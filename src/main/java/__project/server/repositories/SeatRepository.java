package __project.server.repositories;

import __project.server.model.SeatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import __project.server.model.Seat;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, SeatId>{


    @Query(value = "SELECT isAvaliable FROM seat WHERE scheduleId = ?1 AND seatNumber = ?2", nativeQuery = true)
    public Boolean findByScheduleId(int id, int seat_number);

 

    @Modifying
    @Transactional
    @Query(value = "UPDATE seat SET isAvaliable = 0 WHERE scheduleId = ?1 AND seatNumber = ?2", nativeQuery = true)
    public void reserveSeat(int scheduleId, int seatId);



 

}
