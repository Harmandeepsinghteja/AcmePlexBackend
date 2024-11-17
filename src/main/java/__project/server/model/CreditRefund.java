package __project.server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "credit_refund")
public class CreditRefund {

    @Id
    @Column(name = "ticketId")
    int ticketId;

    @Column(name = "refundAmount")
    double refundAmount;

    @Column(name = "expiryDate")
    Date expiryDate;

    public CreditRefund() {}

    public CreditRefund(int ticketId, double refundAmount, Date expiryDate) {
        this.ticketId = ticketId;
        this.refundAmount = refundAmount;
        this.expiryDate = expiryDate;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "CreditRefund{" +
                "ticketId=" + ticketId +
                ", refundAmount=" + refundAmount +
                ", expiryDate=" + expiryDate +
                '}';
    }
}
