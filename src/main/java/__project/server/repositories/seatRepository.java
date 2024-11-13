package __project.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import __project.server.model.seat;


@Repository
public interface seatRepository extends JpaRepository<seat, Integer>{


    @Query(value = "SELECT is_avaliable FROM seat WHERE schedule_id = ?1 AND seat_number = ?2", nativeQuery = true)
    public int findById(int id, int seat_number);
}
