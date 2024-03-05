package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class MusicDto implements Serializable {
    private Long id;
    private String title;
    private String song;
    private String youtube_url;
    private String answer;
    private String hint;
    private String startTime;
    private String endTime;
    private String category;

    @Builder
    public MusicDto(Long id,String title, String song, String youtube_url, String answer, String hint, String startTime, String endTime, String category) {
        this.id = id;
        this.title = title;
        this.song = song;
        this.youtube_url = youtube_url;
        this.answer = answer;
        this.hint = hint;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
    }

}
