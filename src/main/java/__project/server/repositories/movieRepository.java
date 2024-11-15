package __project.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import __project.server.model.movie;

@Repository
public interface movieRepository extends JpaRepository<movie, Integer> {


    @Query(value = "SELECT * FROM movie WHERE addedDate < DATE_SUB(NOW(), INTERVAL 1 WEEK)", nativeQuery = true)
    public List<movie> findMovieAddedGreaterThanOneWeek();

    @Query(value = "SELECT id FROM movie WHERE movieName = ?1", nativeQuery = true)
    public int findByMovieName(String movieName);

    @Query(value = "SELECT * FROM movie WHERE id=?1 AND addedDate < DATE_SUB(NOW(), INTERVAL 1 WEEK)", nativeQuery = true)
    public List<movie> isMovieAddedGreaterThanOneWeek(int id);

    @Query(value = "SELECT * FROM movie WHERE addedDate > DATE_SUB(NOW(), INTERVAL 1 WEEK)", nativeQuery = true)
    public List<movie> findMovieAddedLessThanOneWeek();


}
