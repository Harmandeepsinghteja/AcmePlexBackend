package __project.server.controller;

import __project.server.model.CreditRefund;
import __project.server.model.Ticket;
import __project.server.service.TicketService;
import __project.server.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/ticket")
    public void createTicket(@RequestHeader String token, @RequestBody Ticket ticket) {
        int userId = JwtUtil.verifyJwt(token);
        ticket.setUserId(userId);
        ticketService.bookTicket(ticket);
    }

    /*GET/upcoming-reserved-tickets
    input: token
    [{ticketNumber, MoveTitle, Screen, Seat, PlayTime}, ...]*/

    /*@GetMapping("/upcoming-reserved-tickets")
    public void getUpcomingReservedTickets(@RequestHeader String token) {
        int userId = JwtUtil.verifyJwt(token);
        ticket.setUserId(userId);

    }*/

    @GetMapping("/test")
    public void test() {
        ticketService.sendEmail("basecaper2@gmail.com", "Sent from spring boot", "This email was sent from spring boot");
    }

}
