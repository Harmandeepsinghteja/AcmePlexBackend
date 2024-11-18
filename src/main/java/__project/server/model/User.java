package __project.server.model;

import __project.server.utils.MembershipStatus;
import __project.server.utils.PaymentMethod;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "paymentMethod")
    private PaymentMethod paymentMethod;

    @Column(name = "cardNumber")
    private String cardNumber;

    @Column(name = "membershipExpiryDate")
    private Timestamp membershipExpiryDate;

    @Transient  // This tells spring that age doesn't need to be a column in the user table
    private MembershipStatus memberShipStatus;

    public User() {}

    public User(int id, String email, String password, PaymentMethod paymentMethod, String cardNumber, Timestamp membershipExpiryDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.paymentMethod = paymentMethod;
        this.cardNumber = cardNumber;
        this.membershipExpiryDate = membershipExpiryDate;
        this.memberShipStatus = this.getMembershipStatus();
    }

    public User(String email, String password, PaymentMethod paymentMethod, String cardNumber) {
        this.email = email;
        this.password = password;
        this.paymentMethod = paymentMethod;
        this.cardNumber = cardNumber;
        this.memberShipStatus = this.getMembershipStatus();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Timestamp getMembershipExpiryDate() {
        return membershipExpiryDate;
    }

    public void setMembershipExpiryDate(Timestamp membershipExpiryDate) {
        this.membershipExpiryDate = membershipExpiryDate;
    }

    public MembershipStatus getMembershipStatus() {
        if (membershipExpiryDate == null) return MembershipStatus.NON_PREMIUM;
        else return MembershipStatus.PREMIUM;
    }

    public void setMemberShipStatus(MembershipStatus memberShipStatus) {
        this.memberShipStatus = memberShipStatus;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", membershipExpiryDate=" + membershipExpiryDate +
                ", memberShipStatus='" + memberShipStatus + '\'' +
                '}';
    }
}
