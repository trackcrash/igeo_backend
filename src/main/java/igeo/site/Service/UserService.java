 package igeo.site.Service;

 import igeo.site.DTO.CreateUserDto;
 import igeo.site.DTO.UpdateProfileDto;
 import igeo.site.DTO.UserLoginDto;
 import igeo.site.Provider.JwtTokenProvider;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.core.io.ClassPathResource;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.security.authentication.AuthenticationManager;
 import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.core.AuthenticationException;
 import org.springframework.security.core.context.SecurityContextHolder;
 import org.springframework.security.core.userdetails.UserDetails;
 import org.springframework.security.core.userdetails.UsernameNotFoundException;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.stereotype.Service;
 import igeo.site.Model.User;
 import igeo.site.Repository.UserRepository;
 import org.springframework.transaction.annotation.Transactional;

 import java.io.IOException;
 import java.nio.file.Files;
 import java.nio.file.Paths;
 import java.util.Collections;
 import java.util.List;


 @Service
 public class UserService {
     private final JwtTokenProvider jwtTokenProvider;
     private final UserRepository userRepository;
     private final PasswordEncoder passwordEncoder;
     private final AuthenticationManager authenticationManager;


     @Autowired
     public UserService(JwtTokenProvider jwtTokenProvider, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
         this.jwtTokenProvider = jwtTokenProvider;
         this.userRepository = userRepository;
         this.passwordEncoder = passwordEncoder;
         this.authenticationManager = authenticationManager;
         // 파일에서 금지어 목록을 읽어옵니다.
     }

     // 사용자 저장
     public ResponseEntity<?> save(CreateUserDto createUserDto){

         User user = User.createUser(createUserDto, passwordEncoder);
         User SavedUser = userRepository.save(user);
         return ResponseEntity.ok("User registered successfully with ID: " + SavedUser.getId());
     }
     // 로그인
     public ResponseEntity<String> login(UserLoginDto userLoginDto) {
         try {
             Authentication authentication = authenticationManager.authenticate(
                     new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));
             SecurityContextHolder.getContext().setAuthentication(authentication);
             String token = jwtTokenProvider.generateToken(authentication);
             return ResponseEntity.ok(token);
         } catch (AuthenticationException e) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
         }
     }
     // 로그인된 유저 정보 조회
     public User getLoginUserInfo() {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication == null || "anonymousUser".equals(authentication.getPrincipal().toString())) {
             throw new IllegalArgumentException("로그인이 필요합니다.");
         }
         String email = authentication.getName();
         User user = userRepository.findByEmail(email);
         if (user == null) {
             throw new UsernameNotFoundException("User not found with email: " + email);
         }
         return user;
     }

     // 유저 정보 조회
     public User getUserInfo(String email) {
         User user = userRepository.findByEmail(email);
         if (user == null) {
             throw new UsernameNotFoundException("User not found with email: " + email);
         }
         return user;
     }

     // 유저 삭제
     public String deleteUserByUsername(String username) {
         User user = userRepository.findByEmail(username);
         if (user == null) {
             return "Not Found  User";
         }
         userRepository.delete(user);
         return user.getName();
     }

     // 유저 리스트 조회
     public List<User> getUserList() {
         return userRepository.findAll();
     }

     // 유저 정보 조회
     public User getUserById(Long userId) {
         User user = userRepository.findById(userId).orElse(null);
         if (user == null) {
             throw new IllegalArgumentException("User not found");
         }
         return user;
     }

    //유저 이름으로 유저 조회
    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }

    @Transactional
    public ResponseEntity<?> updateProfile(UpdateProfileDto updateProfileDto)
    {
        User user = getLoginUserInfo();
        if(!passwordEncoder.matches(updateProfileDto.getPassword(),user.getPassword())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Password");
        if(!updateProfileDto.getNewNickName().isBlank())
        {
            for(User item : getUserList())
            {

                if(item != user && item.getName().equals(updateProfileDto.getNewNickName())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 닉네임입니다.");
            }
            user.setName(updateProfileDto.getNewNickName());
        }
        String EncodedNewPassword = passwordEncoder.encode(updateProfileDto.getNewPassword());
        if(!updateProfileDto.getNewPassword().isBlank())
        {
            user.setPassword(EncodedNewPassword);
        }
        userRepository.save(user);
        return ResponseEntity.ok().body("Success");
    }

 }
