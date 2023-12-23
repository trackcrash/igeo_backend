 package igeo.site.DTO;


 import lombok.Builder;
 import lombok.Data;
 import lombok.NoArgsConstructor;

 import java.io.Serializable;
 import javax.validation.constraints.NotBlank;
 import javax.validation.constraints.NotEmpty;

 @NoArgsConstructor
 @Data
 public class CreateUserDto implements Serializable {

     @NotEmpty(message = "이메일을 입력해주세요")
     private String email;

     @NotEmpty(message = "비밀번호는 필수입력 항목입니다.")
     private String password;

     @NotBlank(message = "이름을 입력해주세요")
     private String name;

     @NotEmpty(message = "인증코드를 입력해주세요")
     private String confirmCode;

     @Builder
     public CreateUserDto(String Email, String Password, String Name, String confirmCode) {
         this.email = Email;
         this.password = Password;
         this.name = Name;
         this.confirmCode = confirmCode;
     }

 }

