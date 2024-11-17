package __project.server.service;

import __project.server.model.CreditRefund;
import __project.server.model.Payment;
import __project.server.model.Seat;
import __project.server.model.SeatId;
import __project.server.model.Ticket;
import __project.server.model.User;
import __project.server.model.Schedule;
import __project.server.repositories.CreditRefundRepository;
import __project.server.repositories.PaymentRepository;
import __project.server.repositories.ScheduleRepository;
import __project.server.repositories.SeatRepository;
import __project.server.repositories.TicketRepository;
import __project.server.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.Async;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final PaymentRepository paymentRepository;
    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final CreditRefundRepository creditRefundRepository;
    private final EmailService emailService;

    @Autowired
    public TicketService(
            TicketRepository ticketRepository,
            PaymentRepository paymentRepository,
            ScheduleRepository scheduleRepository,
            SeatRepository seatRepository,
            UserRepository userRepository,
            CreditRefundRepository creditRefundRepository,
            EmailService emailService) {
        this.ticketRepository = ticketRepository;
        this.scheduleRepository = scheduleRepository;
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
        this.creditRefundRepository = creditRefundRepository;
        this.paymentRepository = paymentRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void bookTicket(Ticket ticket) {
        Seat seat = validateTicketDetails(ticket);
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

        // Set seat to unavailable
        seat.setAvailable(false);

        // Send email
        String emailBody = "Your ticket number is: " + savedTicket.getId();
        emailService.sendEmail(user.getEmail(), "Ticket Reservation", emailBody);


    }

    private Seat validateTicketDetails(Ticket ticket) {
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
    }

    public void sendEmail(String to, String subject, String body) {
        emailService.sendEmail(to, subject, body);
    }

}
