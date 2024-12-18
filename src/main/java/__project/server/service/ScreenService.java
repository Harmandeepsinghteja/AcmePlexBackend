package __project.server.service;

import __project.server.model.Screen;
import __project.server.repositories.ScreenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;



@Service
public class ScreenService {
    @Autowired 
    private ScreenRepository screenRepository;

        public String getScreenName(int id) {
        Optional<Screen> screen = screenRepository.findById(id);
        System.out.println("id" + id);
        return screen.get().getScreenName();
        // System.out.println(screen);
        // return " ";
    }
}
