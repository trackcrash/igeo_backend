 package igeo.site.Service;

 import igeo.site.DTO.CreateUserDto;
 import igeo.site.DTO.UserLoginDto;
 import io.jsonwebtoken.Jwts;
 import io.jsonwebtoken.SignatureAlgorithm;
 import lombok.Getter;
 import lombok.RequiredArgsConstructor;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.security.authentication.AuthenticationManager;
 import org.springframework.security.authentication.BadCredentialsException;
 import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.core.AuthenticationException;
 import org.springframework.security.core.context.SecurityContextHolder;
 import org.springframework.security.core.userdetails.UserDetails;
 import org.springframework.security.core.userdetails.UserDetailsService;
 import org.springframework.security.core.userdetails.UsernameNotFoundException;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.stereotype.Service;
 import igeo.site.Model.User;
 import igeo.site.Repository.UserRepository;

 import java.security.SecureRandom;
 import java.time.LocalDateTime;
 import java.util.Base64;
 import java.util.Date;
 import java.util.List;


 @RequiredArgsConstructor
 @Service
 public class UserService implements UserDetailsService {

         @Autowired
         private UserRepository userRepository;

         @Autowired
         private PasswordEncoder passwordEncoder;


         //저장
        public User save(CreateUserDto createUserDto){
            User user = User.createUser(createUserDto, passwordEncoder);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }

         //로그인
         public String Login(UserLoginDto userLoginDto) {
             Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
             authentication = new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword());
             return Jwts.builder()
                     .setSubject(authentication.getName())
                     .claim("Authorities", authentication.getAuthorities())
                     .setIssuedAt(new Date())
                     .setExpiration(new Date(System.currentTimeMillis() + 864000000)) // 토큰 만료 시간 설정
                     .signWith(SignatureAlgorithm.HS256, "igeo".getBytes()) // 시크릿 키 설정
                     .compact();
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

         //유저 삭제
         public void deleteUser(String Email) {
             User user = userRepository.findByEmail(Email);
                userRepository.delete(user);
         }

         //유저 리스트 조회
         public List<User> getUserList() {
             return userRepository.findAll();
         }

         //유저 정보 조회
         @Override
         public UserDetails loadUserByUsername(String Email) throws UsernameNotFoundException {
             User user = userRepository.findByEmail(Email);
             if (user == null) {
                 throw new UsernameNotFoundException("Invalid username/password supplied");
             }
             return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), null);
         }

 }