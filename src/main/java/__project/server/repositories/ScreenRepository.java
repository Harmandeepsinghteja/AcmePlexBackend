package __project.server.repositories;

import __project.server.model.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Integer> {

}
