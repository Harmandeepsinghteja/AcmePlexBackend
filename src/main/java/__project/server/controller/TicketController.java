package __project.server.controller;

import __project.server.model.ReservationDetails;
import __project.server.model.Ticket;
import __project.server.service.TicketService;
import __project.server.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/upcoming-reserved-tickets")
    public List<ReservationDetails> getUpcomingReservedTickets(@RequestHeader String token) {
        int userId = JwtUtil.verifyJwt(token);
        return ticketService.getUpcomingReservedTickets(userId);
    }

    @PostMapping("/ticket")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTicket(@RequestHeader String token, @RequestBody Ticket ticket) {
        int userId = JwtUtil.verifyJwt(token);
        ticket.setUserId(userId);
        ticketService.bookTicket(ticket);
    }

    @PatchMapping("/cancel-ticket/{ticketId}")
    public void cancelTicket(@RequestHeader String token, @PathVariable int ticketId) {
        int userId = JwtUtil.verifyJwt(token);
        ticketService.cancelTicket(ticketId, userId);
    }

}
