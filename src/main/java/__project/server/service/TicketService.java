package __project.server.service;

import __project.server.Entity.ReservationDetails;
import __project.server.model.CreditRefund;
import __project.server.model.Payment;
import __project.server.model.Seat;
import __project.server.model.SeatId;
import __project.server.model.Ticket;
import __project.server.model.User;
import __project.server.repositories.CreditRefundRepository;
import __project.server.repositories.PaymentRepository;
import __project.server.repositories.ScheduleRepository;
import __project.server.repositories.SeatRepository;
import __project.server.repositories.TicketRepository;
import __project.server.repositories.UserRepository;
import __project.server.utils.MembershipStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class TicketService {

    private static final int CANCELLATION_DEADLINE_IN_HOURS = 72;
    private static final double ADMIN_FEE_PERCENTAGE = 0.15;

    private final TicketRepository ticketRepository;
    private final PaymentRepository paymentRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleService scheduleService;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final CreditRefundRepository creditRefundRepository;
    private final EmailService emailService;
    private final SeatService seatService;

    @Autowired
    public TicketService(
            TicketRepository ticketRepository,
            PaymentRepository paymentRepository,
            ScheduleRepository scheduleRepository,
            ScheduleService scheduleService,
            SeatRepository seatRepository,
            UserRepository userRepository,
            CreditRefundRepository creditRefundRepository,
            EmailService emailService,
            SeatService seatService) {
        this.ticketRepository = ticketRepository;
        this.paymentRepository = paymentRepository;
        this.scheduleRepository = scheduleRepository;
        this.scheduleService = scheduleService;
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
        this.creditRefundRepository = creditRefundRepository;

        this.emailService = emailService;
        this.seatService = seatService;
    }

    @Transactional
    public void bookTicket(Ticket ticket) {
        //Seat seat = validateTicketDetails(ticket);
        User user = userRepository.findById(ticket.getUserId()).get();

        // Use up any available credits to purchase this ticket
        double moneySpent = scheduleRepository.findById(ticket.getScheduleId()).get().getPrice();
        double creditSpent = 0;

        List<CreditRefund> creditRefunds = creditRefundRepository.findUnexpiredCreditsByUserId(ticket.getUserId());
        for (CreditRefund creditRefund : creditRefunds) {
            if (moneySpent <= creditRefund.getRefundAmount()) {
                creditRefund.setRefundAmount(creditRefund.getRefundAmount()-moneySpent);
                creditSpent += moneySpent;
                moneySpent = 0;
                break;
            }
            else {
                moneySpent -= creditRefund.getRefundAmount();
                creditSpent += creditRefund.getRefundAmount();
                creditRefund.setRefundAmount(0);
            }
        }

        // Create ticket in db
        Ticket savedTicket = ticketRepository.save(ticket);

        // Create payment in db
        Payment payment = new Payment(
                ticket.getId(),
                new Date(),
                user.getPaymentMethod(),
                user.getCardNumber(),
                creditSpent,
                moneySpent);
        paymentRepository.save(payment);

        // Book seat (it sets seat to be unavailable)
        seatService.reserveSeat(ticket.getScheduleId(), ticket.getSeatNumber());

        // Send email
        String emailBody = "Hello,\n"
                + "This email is a confirmation and receipt of your ticket reservation.\n\n"
                + "Ticket Number: " + savedTicket.getId() + "\n"
                + "Seat Number: " + ticket.getSeatNumber() + "\n"
                + "Movie: " + scheduleService.getMovieName(ticket.getScheduleId()) + "\n"
                + "Start time: " + scheduleService.getStartTime(ticket.getScheduleId()) + "\n\n"
                + "We look forward to seeing you there!\n";

                //"Your ticket number is: " + savedTicket.getId();
        emailService.sendEmail(user.getEmail(), "Acmeplex Ticket Reservation", emailBody);
    }

    public List<ReservationDetails> getUpcomingReservedTickets(int userId) {
        return ticketRepository.findUpcomingReservedTickets(userId);
    }

    // With transaction, you can just fetch a something from the database, and update its properties using the class
    // setters, and those properties will automatically be updated in the database as well, without having to use the
    // .save() method.
    @Transactional
    public void cancelTicket(int ticketId, int userId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "ticket with id " + ticketId + " not found"
        ));

        if (userId != ticket.getUserId()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "ticket with id " + ticketId + " does not belong to this user"
            );
        }

        // Check if cancellation deadline has passed
        if (passedCancellationDeadline(ticket)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cancellation deadline has passed"
            );
        }

        // Cancel ticket
        ticket.setCancelled(true);
        ticket.setCancellationDate(new Date());

        // Make seat available
        Seat seat = seatRepository.findById(new SeatId(ticket.getScheduleId(), ticket.getSeatNumber())).get();
        seat.setAvailable(true);

        // Give credits
        double refundAmount = 0;
        double price = scheduleRepository.findById(ticket.getScheduleId())
                .get()
                .getPrice();
        MembershipStatus membershipStatus = userRepository.findById(userId)
                .get()
                .getMembershipStatus();
        if (membershipStatus.equals(MembershipStatus.NON_PREMIUM)) {
            double administrationFee = price * ADMIN_FEE_PERCENTAGE;
            refundAmount = price - administrationFee;
            refundAmount = Math.round(refundAmount * 100.0) / 100.0;
        }
        CreditRefund creditRefund = new CreditRefund(ticketId, refundAmount, new Date());
        creditRefundRepository.save(creditRefund);
    }

    private boolean passedCancellationDeadline(Ticket ticket) {
        // Get showtime
        Date startTime = scheduleRepository.findById(ticket.getScheduleId())
                .get()
                .getStartTime();

        long diffInMillseconds = Math.abs(new Date().getTime() - startTime.getTime());
        long diff = TimeUnit.HOURS.convert(diffInMillseconds, TimeUnit.MILLISECONDS);
        return diff < 72;
    }

    /*private Seat validateTicketDetails(Ticket ticket) {
        // Check if user exists in db
        if (!userRepository.existsById(ticket.getUserId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }

        // Check if schedule exists in db
        if (!scheduleRepository.existsById(ticket.getScheduleId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule does not exist");
        }
        // Check if seat exists in db and is not already booked
        Optional<Seat> seatOptional = seatRepository.findById(new SeatId(ticket.getScheduleId(), ticket.getSeatNumber()));
        if (!seatOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat does not exist");
        }

        Seat seat = seatOptional.get();
        if (!seat.isAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat is not available");
        }
        return seat;
    }*/

}
