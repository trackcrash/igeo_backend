package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;

@Data
public class StartMissionDto {
    private Long roomId;
    private Long missionId;

    @Builder
    public StartMissionDto(Long roomId, Long missionId) {
        this.roomId = roomId;
        this.missionId = missionId;
    }
}
