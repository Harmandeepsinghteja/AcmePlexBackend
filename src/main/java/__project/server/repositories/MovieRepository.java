package __project.server.repositories;

import __project.server.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {


    @Query(value = "SELECT * FROM movie WHERE addedDate < DATE_SUB(NOW(), INTERVAL 1 WEEK)", nativeQuery = true)
    public List<Movie> findMovieAddedGreaterThanOneWeek();

    @Query(value = "SELECT id FROM movie WHERE movieName = ?1", nativeQuery = true)
    public int findByMovieName(String movieName);

    @Query(value = "SELECT * FROM movie WHERE id=?1 AND addedDate < DATE_SUB(NOW(), INTERVAL 1 WEEK)", nativeQuery = true)
    public List<Movie> isMovieAddedGreaterThanOneWeek(int id);

    @Query(value = "SELECT * FROM movie WHERE addedDate > DATE_SUB(NOW(), INTERVAL 1 WEEK)", nativeQuery = true)
    public List<Movie> findMovieAddedLessThanOneWeek();

    @Query(value = "SELECT movieName FROM movie WHERE id=?1", nativeQuery = true)
    public String findMovieNamebyId(int id);

}
