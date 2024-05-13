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
    private int nextExp;
    private Long userId;

    @Builder
    public RoomUserInfo(String nickname, int character, int level, int exp, int nextExp, Long userId) {
        this.nickname = nickname;
        this.character = character;
        this.level = level;
        this.exp = exp;
        this.nextExp = nextExp;
        this.userId = userId;
    }
}
