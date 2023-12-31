package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Data
public class MissionDto implements Serializable {
    private Long id;
    private String MapName;
    private String MapProducer;
    private String Thumbnail;
    private boolean active;
    private int PlayNum;
    private String Description;
    private Long user_id;
    private List<MusicDto> musics;

    @Builder
    public MissionDto(Long id, String MapName, String MapProducer, String Thumbnail, boolean active, int PlayNum, String Description, Long user_id, List<MusicDto> musics) {
        this.id = id;
        this.MapName = MapName;
        this.MapProducer = MapProducer;
        this.Thumbnail = Thumbnail;
        this.active = active;
        this.PlayNum = PlayNum;
        this.Description = Description;
        this.user_id = user_id;
        this.musics = musics;
    }
}
