package __project.server.service;

import __project.server.model.CreditRefund;
import __project.server.repositories.CreditRefundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditRefundService {

    private final CreditRefundRepository creditRefundRepository;

    @Autowired
    public CreditRefundService(CreditRefundRepository creditRefundRepository) {
        this.creditRefundRepository = creditRefundRepository;
    }

    public List<CreditRefund> getRemainingCredits(int userId) {
        List<CreditRefund> returnedObject = creditRefundRepository.findUnexpiredCreditsByUserId(userId);
        return returnedObject;
    }

}
