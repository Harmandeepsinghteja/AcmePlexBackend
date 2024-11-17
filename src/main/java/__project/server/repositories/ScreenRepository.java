package __project.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import __project.server.model.Screen;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Integer> {

}
