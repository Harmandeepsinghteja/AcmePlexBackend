package __project.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import __project.server.model.seat;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface seatRepository extends JpaRepository<seat, Integer>{


    @Query(value = "SELECT isAvaliable FROM seat WHERE scheduleId = ?1 AND seatNumber = ?2", nativeQuery = true)
    public Boolean findById(int id, int seat_number);


    @Modifying
    @Transactional
    @Query(value = "UPDATE seat SET isAvaliable = 0 WHERE scheduleId = ?1 AND seatNumber = ?2", nativeQuery = true)
    public void reserveSeat(int screenId, int seatId);


}
