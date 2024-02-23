package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;

@Data
public class LoginResponseDto {

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
