 package igeo.site.Service;

 import igeo.site.Config.SpringSecurityConfig;
 import igeo.site.DTO.CreateUserDto;
 import igeo.site.DTO.UserLoginDto;
 import igeo.site.Provider.JwtTokenProvider;
 import io.jsonwebtoken.Jwts;
 import io.jsonwebtoken.SignatureAlgorithm;
 import lombok.Getter;
 import lombok.RequiredArgsConstructor;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.security.authentication.AuthenticationManager;
 import org.springframework.security.authentication.BadCredentialsException;
 import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.core.AuthenticationException;
 import org.springframework.security.core.GrantedAuthority;
 import org.springframework.security.core.authority.SimpleGrantedAuthority;
 import org.springframework.security.core.context.SecurityContext;
 import org.springframework.security.core.context.SecurityContextHolder;
 import org.springframework.security.core.userdetails.UserDetails;
 import org.springframework.security.core.userdetails.UserDetailsService;
 import org.springframework.security.core.userdetails.UsernameNotFoundException;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.stereotype.Component;
 import org.springframework.stereotype.Service;
 import igeo.site.Model.User;
 import igeo.site.Repository.UserRepository;

 import javax.validation.constraints.Email;
 import java.security.SecureRandom;
 import java.time.LocalDateTime;
 import java.util.Base64;
 import java.util.Collections;
 import java.util.Date;
 import java.util.List;
 import java.util.stream.Collectors;


 @RequiredArgsConstructor
 @Service
 public class UserService implements UserDetailsService {
     @Autowired
     private JwtTokenProvider jwtTokenProvider;
     @Autowired
     private UserRepository userRepository;

     @Autowired
     private PasswordEncoder passwordEncoder;


     //저장
    public User save(CreateUserDto createUserDto){
        User user = User.createUser(createUserDto, passwordEncoder);
        user.setPassword(user.getPassword());
        return userRepository.save(user);
    }
     public UserDetails getCurrentUser(Authentication authentication) {
         Object userDetails = authentication.getDetails();

         if (userDetails instanceof UserDetails) {
             return (UserDetails) userDetails;
         }
         return null;
     }
     //로그인
     public ResponseEntity<String> login(UserLoginDto userLoginDto, AuthenticationManager authenticationManager) {
         try {
             // 유저 정보 받기
             UserDetails userDetails = loadUserByUsername(userLoginDto.getEmail());
             Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userLoginDto.getPassword(), userDetails.getAuthorities());
             // 사용자 인증 시도
             Authentication authenticated = authenticationManager.authenticate(authentication);
             // 성공하면 SecurityContextHolder에 인증 정보 설정
             SecurityContextHolder.getContext().setAuthentication(authenticated);
             System.out.println("login " + SecurityContextHolder.getContext().getAuthentication());
             // JWT 토큰 생성 및 반환
             String token = jwtTokenProvider.generateToken(authenticated);
             return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication().getName());
         } catch (AuthenticationException e) {
             // 인증 실패 시 처리
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
         }
     }
     @Override
     public UserDetails loadUserByUsername(String Email) throws UsernameNotFoundException {
         User user = userRepository.findByEmail(Email);
         if (user == null) {
             throw new UsernameNotFoundException("Invalid username/password supplied");
         }
         return new org.springframework.security.core.userdetails.User(
                 user.getEmail(),
                 user.getPassword(),
                 Collections.singletonList(new SimpleGrantedAuthority(user.getPermissions()))
         );
     }

    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }

    //유저 정보 조회
     public User getUserInfo(String Email) {
         return userRepository.findByEmail(Email);
     }

     //유저 정보 수정
     public User updateUserInfo(String Email, User user) {
         User user1 = userRepository.findByEmail(Email);
         user1.setPassword(passwordEncoder.encode(user.getPassword()));
         user1.setName(user.getName());
         user1.setProfile_background(user.getProfile_background());
         userRepository.save(user1);
         return user1;
     }

     //로그인된 유저 정보 조회
     public User getLoginUserInfo() {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication == null || authentication.getName().equals("anonymousUser")) {
             throw new IllegalArgumentException("로그인이 필요합니다.");
         }
        String Email = authentication.getName();
        return userRepository.findByEmail(Email);
    }

     //유저 삭제
     public String deleteUser(String tokenData) {
        try
        {
            String email = jwtTokenProvider.extractUsername(tokenData);
            User user = userRepository.findByEmail(email);
            if(user == null) return "Not Found User";
            userRepository.delete(user);
            return "Success";
        }catch(Exception e)
         {
             e.printStackTrace();
             return "failed";
         }
     }

     //유저 리스트 조회
     public List<User> getUserList() {
         return userRepository.findAll();
     }

     //유저 정보 조회


     public User getUserById(Long userId) {
            return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
     }

     // 클라이언트 요청에 대한 검증
     public ResponseEntity<String> handleClientRequest(String token, String email) {
         UserDetails userDetails = loadUserByUsername(email); // 여기서 실제 사용자 정보를 얻어오는 로직이 들어가야 합니다.
         if (token == null || token.isEmpty()) {
             // 토큰이 없는 경우 401 Unauthorized 반환
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is required");
         }

         if (userDetails == null) {
             // 사용자 정보를 찾을 수 없는 경우 401 Unauthorized 반환
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
         }
         if (jwtTokenProvider.validateToken(token, userDetails)) {
             return ResponseEntity.ok("Valid Token");
         } else {
             // 토큰이 유효하지 않은 경우
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
         }
     }
 }
