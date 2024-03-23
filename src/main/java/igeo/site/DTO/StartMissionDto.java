package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class StartMissionDto implements Serializable {
    private Long roomId;
    private Long missionId;

    @Builder
    public StartMissionDto(Long roomId, Long missionId) {
        this.roomId = roomId;
        this.missionId = missionId;
    }
}
