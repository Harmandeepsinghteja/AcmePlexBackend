package __project.server.service;

import __project.server.model.Movie;
import __project.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementService {

    private final EmailService emailService;
    private final MovieService movieService;
    private final UserService userService;

    @Autowired
    public AnnouncementService(EmailService emailService, MovieService movieService, UserService userService) {
        this.emailService = emailService;
        this.movieService = movieService;
        this.userService = userService;
    }

    // Every 10 seconds: cron = "0/10 * * * * *"
    // Every night at 12 PM: "0 0 12 * * ?"
    @Scheduled(cron = "0 0 12 * * ?")
    public void sendMail() {
        System.out.println("Email sent");
        sendPublicAnnouncement();
        sendPrivateAnnouncement();
    }

    private void sendPublicAnnouncement() {
        List<String> userEmails = userService.getUsers()
                .stream()
                .map(user -> user.getEmail())
                .toList();
        if (userEmails.isEmpty()) {
            return;
        }

        List<Movie> unannouncedPublicMovies = movieService.getPublicMovies()
                .stream()
                .filter(movie -> movie.isPubliclyAnnounced() == false)
                .toList();
        if (unannouncedPublicMovies.isEmpty()) {
            return;
        }

        String emailBody = "Hello Acmeplex user!\n\n"
                + "Here are the latest movies coming to our theatres:\n";
        for (Movie movie : unannouncedPublicMovies) {
            emailBody += (movie.getMovieName() + "\n");
        }
        emailBody += "\nDon't wait, book your ticket now!";

        emailService.sendEmail(userEmails, "Acmeplex New Movie Announcements", emailBody);

        for (Movie movie : unannouncedPublicMovies) {
            movieService.setPubliclyAnnounced(movie.getId(), true);
        }
    }

    private void sendPrivateAnnouncement() {
        List<String> premiumUserEmails = userService.getNonPremiumUsers()
                .stream()
                .map(user -> user.getEmail())
                .toList();
        if (premiumUserEmails.isEmpty()) {
            return;
        }

        List<Movie> unannouncedPrivateMovies = movieService.getNonPublicMovies()
                .stream()
                .filter(movie -> movie.isPrivatelyAnnounced() == false)
                .toList();
        if(unannouncedPrivateMovies.isEmpty()) {
            return;
        }

        String emailBody = "Hello Acmeplex user!\n\n"
                + "Here is an exclusive, early glimpse of the latest movies coming to our theatres:\n";
        for (Movie movie : unannouncedPrivateMovies) {
            emailBody += (movie.getMovieName() + "\n");
        }
        emailBody += "\nSince you are a Premium member, you can start booking these movies right now!";

        emailService.sendEmail(premiumUserEmails, "Acmeplex Early Access Announcements", emailBody);

        for (Movie movie : unannouncedPrivateMovies) {
            movieService.setPrivatelyAnnounced(movie.getId(), true);
        }
    }

}
