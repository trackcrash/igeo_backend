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
    private String imageUrl;
    private String answer;
    private String hint;
    private Float startTime;
    private Float endTime;
    private String category;
    private int CurrentImageIndex;
    private int TotalImageCount;

    @Builder
    public ImageDto(Long id, String title, String imageUrl, String answer, String hint, Float startTime, Float endTime, String category, int CurrentImageIndex, int TotalImageCount) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.answer = answer;
        this.hint = hint;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
        this.CurrentImageIndex = CurrentImageIndex;
        this.TotalImageCount = TotalImageCount;
    }
}
