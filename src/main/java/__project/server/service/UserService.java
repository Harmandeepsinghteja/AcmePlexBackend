package __project.server.service;

import __project.server.model.User;
import __project.server.repositories.UserRepository;
import __project.server.utils.JwtUtil;
import __project.server.utils.PaymentMethod;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String addUser(User newUser) {
        if(userRepository.existsByEmail(newUser.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email taken");
        }
        // Since we used the @GeneratedValue annotation for the id attribute of User, the id in the newUser object will
        // be ignored, and the id will be automatically generated by sql.
        User savedUser = userRepository.save(newUser);
        userRepository.flush();
        String jwt = JwtUtil.generateJwt(savedUser.getId());
        return jwt;
    }

    public String loginUser(User user) {
        this.validateCredentials(user.getEmail(), user.getPassword());
        User savedUser = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        String jwt = JwtUtil.generateJwt(savedUser.getId());
        return jwt;
    }

    public User getUser(int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user not found"
            );
        }
        User user = userOptional.get();
        return userOptional.get();
    }

    // With transaction, you can just fetch a user from the database, and update its properties using the class
    // setters, and those properties will automatically be updated in the database as well.
    @Transactional
    public void updatePassword(int userId, String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be null or empty");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "user not found"
        ));
        user.setPassword(newPassword);
    }

    // With transaction, you can just fetch a user from the database, and update its properties using the class
    // setters, and those properties will automatically be updated in the database as well.
    @Transactional
    public void updatePaymentMethod(int userId, PaymentMethod newPaymentMethod, String newCardNumber) {
        if (newPaymentMethod == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment method cannot be null");
        }
        if (newCardNumber == null || newCardNumber.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card number cannot be null or empty");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "user not found"
        ));
        user.setPaymentMethod(newPaymentMethod);
        user.setCardNumber(newCardNumber);
    }

    private void validateCredentials(String email, String password) {
        // Validate email and password
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email cannot be null or empty"
            );
        }
        if (password == null || password.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Password cannot be null or empty"
            );
        }

        // Check if email and password exists in database
        if (!userRepository.existsByEmailAndPassword(email, password)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Email does not exist or pasword is incorrect"
            );
        }
    }
}
