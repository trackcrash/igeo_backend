package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;

@Data
public class SkipDto {
    private String roomId;
    private String userName;

    @Builder
    public SkipDto(String roomId, String userName) {
        this.roomId = roomId;
        this.userName = userName;
    }
}
