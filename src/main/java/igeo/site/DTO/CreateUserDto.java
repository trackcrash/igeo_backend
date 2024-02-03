 package igeo.site.DTO;


 import lombok.Builder;
 import lombok.Data;
 import lombok.NoArgsConstructor;

 import java.io.Serializable;
 import jakarta.validation.constraints.NotBlank;
 import jakarta.validation.constraints.NotEmpty;

 @NoArgsConstructor
 @Data
 public class CreateUserDto implements Serializable {

     @NotEmpty(message = "이메일을 입력해주세요")
     private String email;

     @NotEmpty(message = "비밀번호는 필수입력 항목입니다.")
     private String password;

     @NotBlank(message = "이름을 입력해주세요")
     private String name;
     @Builder
     public CreateUserDto(String Email, String Password, String Name) {
         this.email = Email;
         this.password = Password;
         this.name = Name;
     }

 }

