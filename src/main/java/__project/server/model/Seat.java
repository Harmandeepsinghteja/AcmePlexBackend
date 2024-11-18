package __project.server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "seat")
@IdClass(SeatId.class)
public class Seat {

    @Id
    @Column(name = "scheduleId")
    private int scheduleId;

    @Id
    @Column(name = "seatNumber")
    private int seatNumber;
    
    @Column(name = "isAvailable")
    private boolean isAvailable;
}
