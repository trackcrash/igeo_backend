package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Data
public class ImageDto implements Serializable {
    private Long id;
    private String title;
    private String image_url;
    private String answer;
    private String hint;
    private Float startTime;
    private Float endTime;
    private String category;

    @Builder
    public ImageDto(Long id,String title, String image_url, String answer, String hint, Float startTime, Float endTime, String category) {
        this.id = id;
        this.title = title;
        this.image_url = image_url;
        this.answer = answer;
        this.hint = hint;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
    }
}
