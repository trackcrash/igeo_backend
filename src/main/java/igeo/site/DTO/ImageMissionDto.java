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
    private String mapName;
    private String mapProducer;
    private String thumbnail;
    private boolean active;
    private int playNum;
    private String description;
    private Long user_id;
    private List<ImageDto> images;
    private int numberOfQuestion;
    private String mapType;

    @Builder
    public ImageMissionDto(Long id, String MapName, String MapProducer, String Thumbnail, boolean active, int PlayNum, String Description, Long user_id, List<ImageDto> images, int numberOfQuestion, String mapType) {
        this.id = id;
        this.mapName = MapName;
        this.mapProducer = MapProducer;
        this.thumbnail = Thumbnail;
        this.active = active;
        this.playNum = PlayNum;
        this.description = Description;
        this.user_id = user_id;
        this.images = images;
        this.numberOfQuestion = numberOfQuestion;
        this.mapType = mapType;
    }
}
