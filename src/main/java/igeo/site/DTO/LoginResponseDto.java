package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;

@Data
public class LoginResponseDto {

    private String Token;
    private int Level;
    private String Nickname;

    @Builder
    public LoginResponseDto(String Token, int Level, String Nickname) {
        this.Token = Token;
        this.Level = Level;
        this.Nickname = Nickname;
    }
}
