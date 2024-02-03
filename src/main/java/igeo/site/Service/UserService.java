 package igeo.site.Service;

 import igeo.site.DTO.CreateUserDto;
 import igeo.site.DTO.UserLoginDto;
 import igeo.site.Provider.JwtTokenProvider;
 import lombok.RequiredArgsConstructor;
 import org.springframework.beans.factory.annotation.Autowired;
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

 import java.util.List;
 import java.util.stream.Collectors;


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
     }

     // 사용자 저장
     public User save(CreateUserDto createUserDto){
         User user = User.createUser(createUserDto, passwordEncoder);
         return userRepository.save(user);
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
             return "Not Found User";
         }
         userRepository.delete(user);
         return "Success";
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

     // 클라이언트 요청에 대한 검증
     public ResponseEntity<String> handleClientRequest(String token, String email) {
         UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         if (!jwtTokenProvider.validateToken(token, userDetails)) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
         }
         return ResponseEntity.ok("Valid Token");
     }

    //유저 이름으로 유저 조회
    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }

 }
