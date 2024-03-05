package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@NoArgsConstructor
@Data
public class MissionSelectDto implements Serializable {
    private Long id;
    private String thumbnail;
    private String mapName;
    private String description;
    private String mapType;
    private int numberOfQuestion;

    @Builder
    public MissionSelectDto(Long id, String Thumbnail, String MapName, String Description, String MapType, int numberOfQuestion) {
        this.id = id;
        thumbnail = Thumbnail;
        mapName = MapName;
        description = Description;
        mapType = MapType;
        this.numberOfQuestion = numberOfQuestion;
    }

}
