package igeo.site.Model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="missionTable")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mapName;
    private String mapProducer;
    private String thumbnail;
    private boolean active;
    private int playNum;
    private String description;
    private int numberOfQuestion;
    private String mapType;

    @ManyToOne
    @JoinColumn(name = "mapProducer_id", referencedColumnName = "id")
    private User user;

    @Builder
    public Mission(Long id, String mapName, String mapProducer, String thumbnail, boolean active, int playNum, String description, User user, int numberOfQuestion, String mapType) {
        this.id = id;
        this.mapName = mapName;
        this.mapProducer = mapProducer;
        this.thumbnail = thumbnail;
        this.active = active;
        this.playNum = playNum;
        this.description = description;
        this.user = user;
        this.numberOfQuestion = numberOfQuestion;
        this.mapType = mapType;
    }

}
