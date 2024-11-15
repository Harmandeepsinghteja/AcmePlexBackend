package __project.server.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import __project.server.model.schedule;
import java.util.List;

@Repository
public interface scheduleRepository extends JpaRepository<schedule, Integer> {
    
    

    @Query(value = "SELECT * FROM schedule WHERE movieId = ?1", nativeQuery = true)
    List<schedule> findByMovieId(int id);

    @Query(value = "SELECT * FROM schedule WHERE LOWER(movieName)=LOWER(?1)", nativeQuery = true)
    List<schedule> findByMovieName(String movieName);

}
