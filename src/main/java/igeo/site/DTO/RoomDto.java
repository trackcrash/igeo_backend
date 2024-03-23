package igeo.site.DTO;

import igeo.site.Model.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class RoomDto implements Serializable {

    private CreateRoomDto.RoomType type;
    private String roomId;
    private String password;

    @Builder
    public RoomDto(CreateRoomDto.RoomType type, String roomId, String password) {
        this.type = type;
        this.roomId = roomId;
        this.password = password;
    }
}
