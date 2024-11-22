package __project.server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;




@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "movieName")
    private String movieName;

    @Column(name = "addedDate")
    private Date addedDate;

    @Column(name = "url")
    private String url;

    @Column(name = "privatelyAnnounced")
    private boolean privatelyAnnounced;

    @Column(name = "publiclyAnnounced")
    private boolean publiclyAnnounced;
}
