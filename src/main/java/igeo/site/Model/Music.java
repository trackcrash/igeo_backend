package igeo.site.Model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//음악 테이블 - 미션 테이블과 1:N 관계
//Lombok 사용됨
@Entity
@NoArgsConstructor
@Getter
@Table(name="MusicTable")
public class Music {

    //PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String song;
    private String youtube_url;
    private String thumbnail_url;
    private String answer;
    private String hint;
    //Fk - Mission
    @ManyToOne
    @JoinColumn(name = "mission_id", referencedColumnName = "id")
    private Mission mission;
    private Float startTime;
    private Float endTime;
    private String category;

    //Builder pattern
    @Builder
    public Music(Long id, String title, String song, String youtube_url, String thumbnail_url, String answer, String hint, Mission mission, Float startTime, Float endTime, String category) {
        this.id = id;
        this.title = title;
        this.song = song;
        this.youtube_url = youtube_url;
        this.thumbnail_url = thumbnail_url;
        this.answer = answer;
        this.hint = hint;
        this.mission = mission;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
    }

    //Create method
    public static Music createMusic(String title, String song, String youtube_url, String thumbnail_url, String answer, String hint, Mission mission, Float startTime, Float endTime, String category) {
        Music music = Music.builder()
                .title(title)
                .song(song)
                .youtube_url(youtube_url)
                .thumbnail_url(thumbnail_url)
                .answer(answer)
                .hint(hint)
                .mission(mission)
                .startTime(startTime)
                .endTime(endTime)
                .category(category)
                .build();
        return music;
    }
}


