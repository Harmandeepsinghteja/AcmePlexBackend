package __project.server.controller;

import __project.server.model.CreditRefund;
import __project.server.service.CreditRefundService;
import __project.server.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CreditRefundController {

    private final CreditRefundService creditRefundService;

    @Autowired
    public CreditRefundController(CreditRefundService creditRefundService) {
        this.creditRefundService = creditRefundService;
    }

    @GetMapping("/remaining-credits")
    public List<CreditRefund> getRemainingCredits(@RequestHeader("token") String token) {
        int userId = JwtUtil.verifyJwt(token);
        return creditRefundService.getRemainingCredits(userId);
    }


}
