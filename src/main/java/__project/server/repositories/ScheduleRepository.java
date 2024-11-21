package __project.server.repositories;

import __project.server.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    
    

    @Query(value = "SELECT * FROM schedule WHERE movieId = ?1", nativeQuery = true)
    List<Schedule> findByMovieId(int id);

    @Query(value = "SELECT * FROM schedule WHERE LOWER(movieName)=LOWER(?1)", nativeQuery = true)
    List<Schedule> findByMovieName(String movieName);


    @Query(value = "SELECT id FROM schedule WHERE movieId = ?1 AND screenId = ?2 AND startTime = ?3", nativeQuery = true)
    Integer findByMovieScreenDate(int movieId, int screenId, Timestamp time);

    @Query(value = "SELECT price FROM schedule WHERE id = ?1", nativeQuery = true)
    int getPrice(int scheduleId);

    @Query(value = "SELECT startTime FROM schedule WHERE id = ?1", nativeQuery = true)
    Timestamp getStartTime(int scheduleId);

    @Query(value = "SELECT screenId FROM schedule WHERE id = ?1", nativeQuery = true)
    int getScreenId(int scheduleId);

}
