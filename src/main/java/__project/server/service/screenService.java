package __project.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import __project.server.repositories.screenRepository;
import __project.server.model.screen;
import java.util.Optional;



@Service
public class screenService {
    @Autowired 
    private screenRepository screenRepository;

        public String getScreenName(int id) {
        Optional<screen> screen = screenRepository.findById(id);
        System.out.println("id" + id);
        return screen.get().getScreenName();
        // System.out.println(screen);
        // return " ";
    }
}
