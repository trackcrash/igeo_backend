package igeo.site.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
public class TokenRequestDto {
    @NotBlank(message = "Token must not be blank")
    String token;
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    String email;
}
