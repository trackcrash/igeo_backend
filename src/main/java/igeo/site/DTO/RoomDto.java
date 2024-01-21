package igeo.site.DTO;

import igeo.site.Model.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomDto {

    private CreateRoomDto.RoomType type;
    private String roomId;
    private String password;
    private Long userId;

    @Builder
    public RoomDto(CreateRoomDto.RoomType type, String roomId, Long userId, String password) {
        this.type = type;
        this.roomId = roomId;
        this.password = password;
        this.userId = userId;
    }
}
