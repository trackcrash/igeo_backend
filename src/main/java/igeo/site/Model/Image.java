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
@Table(name="ImageTable")
public class Image {
    //PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String imageUrl;
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
    public Image(Long id, String title, String imageUrl, String answer, String hint, Mission mission, Float startTime, Float endTime, String category) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.answer = answer;
        this.hint = hint;
        this.mission = mission;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
    }
}


