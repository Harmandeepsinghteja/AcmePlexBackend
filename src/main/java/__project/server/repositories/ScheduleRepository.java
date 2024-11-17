package __project.server.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import __project.server.model.schedule;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<schedule, Integer> {
    
    

    @Query(value = "SELECT * FROM schedule WHERE movieId = ?1", nativeQuery = true)
    List<schedule> findByMovieId(int id);

    @Query(value = "SELECT * FROM schedule WHERE LOWER(movieName)=LOWER(?1)", nativeQuery = true)
    List<schedule> findByMovieName(String movieName);


    @Query(value = "SELECT id FROM schedule WHERE movieId = ?1 AND screenId = ?2 AND startTime = ?3", nativeQuery = true)
    Integer findByMovieScreenDate(int movieId, int screenId, Timestamp time);

}
