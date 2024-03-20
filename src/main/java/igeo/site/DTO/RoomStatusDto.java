package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
public class RoomStatusDto {
    private CreateRoomDto.RoomType type;
    private String roomId;
    private String roomName;
    private String owner;
    private String password;
    private int maxUsers;
    private List<RoomUserInfo> currentUsers;
    private Long missionId;

    @Builder
    public RoomStatusDto(CreateRoomDto.RoomType type, String roomId, String roomName, String owner, String password, int maxUsers, List<RoomUserInfo> currentUsers, Long missionId) {
        this.type = type;
        this.roomId = roomId;
        this.roomName = roomName;
        this.owner = owner;
        this.password = password;
        this.maxUsers = maxUsers;
        this.currentUsers = currentUsers;
        this.missionId = missionId;
    }
}
