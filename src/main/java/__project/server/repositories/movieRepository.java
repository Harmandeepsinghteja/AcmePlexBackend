package __project.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import __project.server.model.movie;

@Repository
public interface movieRepository extends JpaRepository<movie, Integer> {


    @Query(value = "SELECT * FROM movie WHERE added_date < DATE_SUB(NOW(), INTERVAL 1 WEEK)", nativeQuery = true)
    public List<movie> findMovieAddedGreaterThanOneWeek();
}
