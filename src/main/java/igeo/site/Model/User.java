 package igeo.site.Model;

 import lombok.Builder;
 import lombok.Getter;
 import lombok.NoArgsConstructor;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import igeo.site.DTO.CreateUserDto;
 import jakarta.persistence.*;
 import java.time.LocalDateTime;

 @NoArgsConstructor
 @Entity
 @Getter
 public class User {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
     private String name;
     private String password;
     private String email;
     private int level;
     private int exp;
     private int nextExp;
     private int character;
     private LocalDateTime lastLogin;
     private boolean isGoogleAuthenticated;
     private String permissions;
     private String profileBackground;

     @Builder
     public User(Long id, String name, String password, String email, int level, int exp, int nextExp, int character, LocalDateTime lastLogin, boolean isGoogleAuthenticated, String permissions, String profileBackground) {
         this.id = id;
         this.name = name;
         this.password = password;
         this.email = email;
         this.level = level;
         this.exp = exp;
         this.nextExp = nextExp;
         this.character = character;
         this.lastLogin = lastLogin;
         this.isGoogleAuthenticated = isGoogleAuthenticated;
         this.permissions = permissions;
         this.profileBackground = profileBackground;
     }

     public static User createUser(CreateUserDto createUserDto, PasswordEncoder passwordEncoder){
         User user = User.builder()
                 .name(createUserDto.getName())
                 .password(passwordEncoder.encode((createUserDto.getPassword())))
                 .email(createUserDto.getEmail())
                 .level(0)
                 .exp(0)
                 .nextExp(10)
                 .character(0)
                 .lastLogin(LocalDateTime.now())
                 .isGoogleAuthenticated(false)
                 .permissions("0")
                 .profileBackground("")
                 .build();
         return user;
     }


 }
