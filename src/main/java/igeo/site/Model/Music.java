package igeo.site.Model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//음악 테이블 - 미션 테이블과 1:N 관계
//Lombok 사용됨
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="MusicTable")
public class Music {

    //PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String song;
    private String youtube_url;
    private String answer;
    private String hint;
    //Fk - Mission
    @ManyToOne
    @JoinColumn(name = "mission_id", referencedColumnName = "id")
    private Mission mission;
    private String startTime;
    private String endTime;
    private String category;

    //Builder pattern
    @Builder
    public Music(Long id, String title, String song, String youtube_url, String answer, String hint, Mission mission, String startTime, String endTime, String category) {
        this.id = id;
        this.title = title;
        this.song = song;
        this.youtube_url = youtube_url;
        this.answer = answer;
        this.hint = hint;
        this.mission = mission;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
    }

}


