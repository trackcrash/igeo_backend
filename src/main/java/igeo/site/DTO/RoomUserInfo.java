package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;

@Data
public class RoomUserInfo {
    private String nickname;
    private int character;
    private int level;

    @Builder
    public RoomUserInfo(String nickname, int character, int level) {
        this.nickname = nickname;
        this.character = character;
        this.level = level;
    }
}
