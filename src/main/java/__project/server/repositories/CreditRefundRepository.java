package __project.server.repositories;

import __project.server.model.CreditRefund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditRefundRepository extends JpaRepository<CreditRefund, Integer> {


    @Query(value = "" +
            """
            SELECT credit_refund.ticketId, credit_refund.refundAmount, credit_refund.expiryDate
                        FROM credit_refund
            JOIN ticket on credit_refund.ticketId = ticket.id
            WHERE ticket.userId = ?1 AND credit_refund.expiryDate >= NOW() AND credit_refund.refundAmount > 0
            ORDER BY credit_refund.expiryDate;
            """
            , nativeQuery = true)
    public List<CreditRefund> findUnexpiredCreditsByUserId(int userId);

}
