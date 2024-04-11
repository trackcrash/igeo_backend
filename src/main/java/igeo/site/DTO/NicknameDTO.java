package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;
@Data
public class NicknameDTO implements Serializable {
    @Size(max = 8, message = "이름은 8글자 이하로 해주세요.")
    private String name;

    @Builder
    public NicknameDTO(String name) {
        this.name = name;
    }
}
