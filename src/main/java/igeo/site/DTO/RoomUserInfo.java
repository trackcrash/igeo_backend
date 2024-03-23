package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class RoomUserInfo implements Serializable {
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
