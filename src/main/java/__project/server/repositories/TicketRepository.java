package __project.server.repositories;

import __project.server.Entity.ReservationDetails;
import __project.server.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    @Query(value = "" +
            """
            SELECT ticket.id as ticketId, movie.movieName as movieName, screen.screenName as screenNumber, ticket.seatNumber, schedule.startTime
            FROM ticket
            JOIN schedule on schedule.id = ticket.scheduleId
            JOIN movie on movie.id = schedule.movieId
            JOIN screen on screen.id = schedule.screenId
            WHERE ticket.userId = ?1 AND schedule.startTime >= NOW() AND ticket.isCancelled = FALSE;
            """
            , nativeQuery = true)
    public List<ReservationDetails> findUpcomingReservedTickets(int userId);

    //public Optional<Ticket> findFirst1ByIdAndUserId(int ticketId, int userId);
}
