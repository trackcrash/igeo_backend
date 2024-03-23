package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Data
@NoArgsConstructor
public class RoomListDto implements Serializable {

    private String roomId;
    private CreateRoomDto.RoomType type;
    private String roomName;
    private String owner;
    private String password;
    private int maxUsers;
    private Long currentUsers;
    private String mapType;
    private String thumbnail;

    @Builder
    public RoomListDto(String roomId, CreateRoomDto.RoomType type, String roomName, String owner, String password, int maxUsers, Long currentUsers, String mapType, String thumbnail) {
        this.roomId = roomId;
        this.type = type;
        this.roomName = roomName;
        this.owner = owner;
        this.password = password;
        this.maxUsers = maxUsers;
        this.currentUsers = currentUsers;
        this.mapType = mapType;
        this.thumbnail = thumbnail;
    }
}
