package __project.server.model;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
public class SeatId implements Serializable {

    
    private Integer scheduleId;
    private Integer seatNumber;

    // Constructors, equals, and hashCode methods
}