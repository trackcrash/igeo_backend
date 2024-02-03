package igeo.site.DTO;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateProfileDto {
    private String newNickName;
    @NotEmpty(message = "비밀번호는 필수입력 항목입니다.")
    private String password;
    private String newPassword;
}
