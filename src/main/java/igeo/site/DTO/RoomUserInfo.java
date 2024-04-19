package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class RoomUserInfo implements Serializable {
    private String nickname;
    private int character;
    private int level;
    private int exp;

    @Builder
    public RoomUserInfo(String nickname, int character, int level, int exp) {
        this.nickname = nickname;
        this.character = character;
        this.level = level;
        this.exp = exp;
    }
}
