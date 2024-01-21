package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateRoomDto {
    private RoomType type;
    private String roomName;
    private String sender;
    private String password;
    private int maxUser;

    public enum RoomType {
        PUBLIC, PRIVATE;
    }

    @Builder
    public CreateRoomDto(RoomType type, String roomName, String sender, String password, int maxUser) {
        this.type = type;
        this.roomName = roomName;
        this.sender = sender;
        this.password = password;
        this.maxUser = maxUser;
    }
}
