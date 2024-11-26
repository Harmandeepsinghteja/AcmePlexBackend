package __project.server.service;

import __project.server.model.CreditRefund;
import __project.server.repositories.CreditRefundRepository;
import __project.server.utils.MembershipStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CreditRefundService {

    private static final double ADMIN_FEE_PERCENTAGE = 0.15;

    private final CreditRefundRepository creditRefundRepository;

    @Autowired
    public CreditRefundService(CreditRefundRepository creditRefundRepository) {
        this.creditRefundRepository = creditRefundRepository;
    }

    public List<CreditRefund> getRemainingCredits(int userId) {
        List<CreditRefund> returnedObject = creditRefundRepository.findUnexpiredCreditsByUserId(userId);
        return returnedObject;
    }

    public void createCreditRefund(int ticketId, double originalPrice, MembershipStatus membershipStatus) {
        double refundAmount = originalPrice;

        if (membershipStatus.equals(MembershipStatus.NON_PREMIUM)) {
            double administrationFee = originalPrice * ADMIN_FEE_PERCENTAGE;
            refundAmount -= administrationFee;
            refundAmount = Math.round(refundAmount * 100.0) / 100.0;
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1); // to get previous year add -1
        Date expirationDate = cal.getTime();
        CreditRefund creditRefund = new CreditRefund(ticketId, refundAmount, expirationDate);
        creditRefundRepository.save(creditRefund);
    }

    @Transactional
    public double useUpCredits(int userId, double price) {
        double moneySpent = price;
        double creditSpent = 0;

        List<CreditRefund> creditRefunds = creditRefundRepository.findUnexpiredCreditsByUserId(userId);
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
        return creditSpent;
    }

}
