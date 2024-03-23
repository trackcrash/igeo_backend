package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class LoginResponseDto implements Serializable {

    private String Token;
    private int Level;
    private String Nickname;
    private int Character;
    @Builder
    public LoginResponseDto(String Token, int Level, String Nickname, int Character) {
        this.Token = Token;
        this.Level = Level;
        this.Nickname = Nickname;
        this.Character = Character;
    }
}
