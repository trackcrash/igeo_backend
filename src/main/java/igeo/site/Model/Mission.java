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
@Table(name="MissionTable")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String MapName;
    private String MapProducer;
    private String Thumbnail;
    private boolean active;
    private int PlayNum;
    private String Description;
    private int numberOfQuestion;
    private String mapType;

    @ManyToOne
    @JoinColumn(name = "MapProducer_id", referencedColumnName = "id")
    private User user;

    @Builder
    public Mission(Long id, String MapName, String MapProducer, String Thumbnail, boolean active, int PlayNum, String Description, User user, int numberOfQuestion, String mapType) {
        this.id = id;
        this.MapName = MapName;
        this.MapProducer = MapProducer;
        this.Thumbnail = Thumbnail;
        this.active = active;
        this.PlayNum = PlayNum;
        this.Description = Description;
        this.user = user;
        this.numberOfQuestion = numberOfQuestion;
        this.mapType = mapType;
    }

    public static Mission createMission(String MapName, String MapProducer, String Thumbnail, boolean active, int PlayNum, String Description, User user, int numberOfQuestion, String mapType) {
        Mission mission = Mission.builder()
                .MapName(MapName)
                .MapProducer(MapProducer)
                .Thumbnail(Thumbnail)
                .active(active)
                .PlayNum(PlayNum)
                .Description(Description)
                .user(user)
                .numberOfQuestion(numberOfQuestion)
                .mapType(mapType)
                .build();
        return mission;
    }
}
