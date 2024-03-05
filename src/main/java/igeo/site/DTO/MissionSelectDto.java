package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@NoArgsConstructor
@Data
public class MissionSelectDto implements Serializable {
    private Long id;
    private String Thumbnail;
    private String MapName;
    private String Description;
    private String MapType;
    private int numberOfQuestion;

    @Builder
    public MissionSelectDto(Long id, String thumbnail, String mapName, String description, String mapType, int numberOfQuestion) {
        this.id = id;
        Thumbnail = thumbnail;
        MapName = mapName;
        Description = description;
        MapType = mapType;
        this.numberOfQuestion = numberOfQuestion;
    }

}
