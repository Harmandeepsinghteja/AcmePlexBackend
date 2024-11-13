package __project.server.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(seatId.class)
public class seat {

    @Id
    private int scheduleId;
    @Id
    private int seatNumber;
    
    private int isAvaliable;

}
