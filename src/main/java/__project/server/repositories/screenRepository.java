package __project.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import __project.server.model.schedule;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import __project.server.model.screen;

@Repository
public interface screenRepository extends JpaRepository<screen, Integer> {

}
