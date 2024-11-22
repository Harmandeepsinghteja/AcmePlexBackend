package __project.server.controller;

import __project.server.model.User;
import __project.server.service.UserService;
import __project.server.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerUser(@RequestBody User newUser) {
        String jwt = userService.addUser(newUser);
        return jwt;
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
        String jwt = userService.loginUser(user);
        return jwt;
    }

    @GetMapping("/user")
    public User getUser(@RequestHeader("token") String token) {
        int userId = JwtUtil.verifyJwt(token);
        return userService.getUser(userId);
    }

    @PatchMapping("/user-password")
    public void updatePassword(@RequestBody User user, @RequestHeader("token") String token) {
        int userId = JwtUtil.verifyJwt(token);
        userService.updatePassword(userId, user.getPassword());
    }

    @PatchMapping("payment-method")
    public void updatePaymentMethod(@RequestBody User user, @RequestHeader("token") String token) {
        int userId = JwtUtil.verifyJwt(token);
        userService.updatePaymentMethod(userId, user.getPaymentMethod(), user.getCardNumber());
    }

    @PatchMapping("buy-premium")
    public void setMembershipStatusToPremium(@RequestHeader("token") String token) {
        int userId = JwtUtil.verifyJwt(token);
        userService.setMembershipStatusToPremium(userId);
    }

}
