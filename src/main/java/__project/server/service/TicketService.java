package __project.server.service;

import __project.server.Entity.ReservationDetails;
import __project.server.model.Payment;
import __project.server.model.Ticket;
import __project.server.model.User;
import __project.server.repositories.TicketRepository;
import __project.server.utils.MembershipStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TicketService {

    private static final int CANCELLATION_DEADLINE_IN_HOURS = 72;

    private final TicketRepository ticketRepository;
    private final PaymentService paymentService;
    private final ScheduleService scheduleService;
    private final UserService userService;
    private final CreditRefundService creditRefundService;
    private final EmailService emailService;
    private final SeatService seatService;
    private final MovieService movieService;


    @Autowired
    public TicketService(
            TicketRepository ticketRepository,
            PaymentService paymentService,
            ScheduleService scheduleService,
            UserService userService,
            CreditRefundService creditRefundService,
            EmailService emailService,
            SeatService seatService
            ,MovieService movieService
            ) {
        this.ticketRepository = ticketRepository;
        this.paymentService = paymentService;
        this.scheduleService = scheduleService;
        this.userService = userService;
        this.creditRefundService = creditRefundService;
        this.emailService = emailService;
        this.seatService = seatService;
        this.movieService = movieService;
    }

    @Transactional
    public void bookTicket(Ticket ticket) {
        //Seat seat = validateTicketDetails(ticket);

        // Return 400 if schedule time has already passed
        if (scheduleService.getStartTime(ticket.getScheduleId()).before(new Date())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Showtime has has passed"
            );
        }

        User user = userService.getUser(ticket.getUserId());

        // Use up any available credits to purchase this ticket
        double price = scheduleService.getPrice(ticket.getScheduleId());
        double creditSpent = creditRefundService.useUpCredits(ticket.getUserId(), price);
        double moneySpent = price - creditSpent;

        // Create ticket in db
        Ticket savedTicket = ticketRepository.save(ticket);

        // Create payment in db
        Payment payment = new Payment(
                savedTicket.getId(),
                new Date(),
                user.getPaymentMethod(),
                user.getCardNumber(),
                creditSpent,
                moneySpent);
        paymentService.addPayment(payment);

        int movieId = scheduleService.getMovieId(ticket.getScheduleId());

        // If user is non-premium and movie is non-public, then throw exception
        if (!movieService.isMoviePublic(movieId) && user.getMembershipStatus().equals(MembershipStatus.NON_PREMIUM) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Non-premium users cannot book movies that are still private");
        }

        // If Movie is non public-> check if the nonpublic seats filled or not. If filled throw exception
        // otherwise book seat
         if(!movieService.isMoviePublic(movieId) && seatService.isNonPublicSeatsFilled(ticket.getScheduleId())){
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please Book Movie After Public Announcement");
         }

        // Book seat (it sets seat to be unavailable)
        // Check if user is premium
        /*if (user.getMembershipStatus().equals(MembershipStatus.PREMIUM) && ) {

        }*/
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
        seatService.makeSeatAvailable(ticket.getScheduleId(), ticket.getSeatNumber());

        // Give credits
        double price = scheduleService.getPrice(ticket.getScheduleId());
        MembershipStatus membershipStatus = userService.getUser(userId).getMembershipStatus();
        creditRefundService.createCreditRefund(ticketId, price, membershipStatus);
    }

    private boolean passedCancellationDeadline(Ticket ticket) {
        // Get showtime
        Date startTime = scheduleService.getStartTime(ticket.getScheduleId());

        long diffInMillseconds = Math.abs(new Date().getTime() - startTime.getTime());
        long diff = TimeUnit.HOURS.convert(diffInMillseconds, TimeUnit.MILLISECONDS);
        return diff < CANCELLATION_DEADLINE_IN_HOURS;
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
