package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class SkipDto implements Serializable {
    private String roomId;
    private String userName;

    @Builder
    public SkipDto(String roomId, String userName) {
        this.roomId = roomId;
        this.userName = userName;
    }
}
