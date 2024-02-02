package igeo.site.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class UserLoginDto {
    private String email;
    private String password;

    // 생성자, 게터, 세터 등 필요한 메서드 추가
}