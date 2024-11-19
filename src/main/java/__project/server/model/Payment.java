package __project.server.model;

import __project.server.utils.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "ticketId")
    private int ticketId;

    @Column(name = "paymentTime")
    private Date paymentTime;

    @Column(name = "paymentMethod")
    private PaymentMethod paymentMethod;

    @Column(name = "cardNumber")
    private String cardNumber;

    @Column(name = "creditSpent")
    private double creditSpent;

    @Column(name = "moneySpent")
    private double moneySpent;

    public Payment() {}

    public Payment(
            int id,
            int ticketId,
            Date paymentTime,
            PaymentMethod paymentMethod,
            String cardNumber,
            double creditSpent,
            double moneySpent) {
        this.id = id;
        this.ticketId = ticketId;
        this.paymentTime = paymentTime;
        this.paymentMethod = paymentMethod;
        this.cardNumber = cardNumber;
        this.creditSpent = creditSpent;
        this.moneySpent = moneySpent;
    }

    public Payment(int ticketId, Date paymentTime, PaymentMethod paymentMethod, String cardNumber, double creditSpent, double moneySpent) {
        this.ticketId = ticketId;
        this.paymentTime = paymentTime;
        this.paymentMethod = paymentMethod;
        this.cardNumber = cardNumber;
        this.creditSpent = creditSpent;
        this.moneySpent = moneySpent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public double getCreditSpent() {
        return creditSpent;
    }

    public void setCreditSpent(double creditSpent) {
        this.creditSpent = creditSpent;
    }

    public double getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(double moneySpent) {
        this.moneySpent = moneySpent;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", ticketId=" + ticketId +
                ", paymentTime=" + paymentTime +
                ", paymentMethod=" + paymentMethod +
                ", cardNumber='" + cardNumber + '\'' +
                ", creditSpent=" + creditSpent +
                ", moneySpent=" + moneySpent +
                '}';
    }

}
