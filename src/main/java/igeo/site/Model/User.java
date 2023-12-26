 package igeo.site.Model;

 import lombok.Builder;
 import lombok.Getter;
 import lombok.NoArgsConstructor;
 import lombok.Setter;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import igeo.site.DTO.CreateUserDto;
 import jakarta.persistence.*;
 import java.time.LocalDateTime;

 @NoArgsConstructor
 @Entity
 @Getter
 @Setter
 @Table(name="UserTable")
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
     private LocalDateTime last_login;
     private boolean is_google_authenticated;
     private String permissions;
     private String profile_background;

     @Builder
     public User(Long id, String name, String password, String email, int level, int exp, int nextExp, int character, LocalDateTime last_login, boolean is_google_authenticated, String permissions, String profile_background) {
         this.id = id;
         this.name = name;
         this.password = password;
         this.email = email;
         this.level = level;
         this.exp = exp;
         this.nextExp = nextExp;
         this.character = character;
         this.last_login = last_login;
         this.is_google_authenticated = is_google_authenticated;
         this.permissions = permissions;
         this.profile_background = profile_background;
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
                 .last_login(LocalDateTime.now())
                 .is_google_authenticated(false)
                 .permissions("0")
                 .profile_background("")
                 .build();
         return user;
     }
 }
