package __project.server.model;

import java.util.Date;

public interface ReservationDetails {
    /*
    * This interface is used to get the result of the CreditRefundRepository.findUnexpiredCreditsByUserId() query.
    * If we want to store the results of a query where the result doesn't map to a table in the database, we have
    * to create an interface and set getters for each column of the result
    */
    public Integer getTicketId();
    public String getMovieName();
    public Integer getScreenNumber();
    public Date getStartTime();
    public Integer getSeatNumber();
}
