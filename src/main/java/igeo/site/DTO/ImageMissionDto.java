package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Data
public class ImageMissionDto implements Serializable {
    private Long id;
    private String MapName;
    private String MapProducer;
    private String Thumbnail;
    private boolean active;
    private int PlayNum;
    private String Description;
    private Long user_id;
    private List<ImageDto> images;
    private int numberOfQuestion;
    private String mapType;

    @Builder
    public ImageMissionDto(Long id, String MapName, String MapProducer, String Thumbnail, boolean active, int PlayNum, String Description, Long user_id, List<ImageDto> images, int numberOfQuestion, String mapType) {
        this.id = id;
        this.MapName = MapName;
        this.MapProducer = MapProducer;
        this.Thumbnail = Thumbnail;
        this.active = active;
        this.PlayNum = PlayNum;
        this.Description = Description;
        this.user_id = user_id;
        this.images = images;
        this.numberOfQuestion = numberOfQuestion;
        this.mapType = mapType;
    }
}
