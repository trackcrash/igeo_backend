 package igeo.site.Service;

 import igeo.site.DTO.*;
 import igeo.site.Model.CustumOAuth2User;
 import igeo.site.Model.Room;
 import igeo.site.Provider.JwtTokenProvider;
 import jakarta.servlet.http.HttpServletResponse;
 import lombok.RequiredArgsConstructor;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.core.io.ClassPathResource;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.security.authentication.AnonymousAuthenticationToken;
 import org.springframework.security.authentication.AuthenticationManager;
 import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.core.AuthenticationException;
 import org.springframework.security.core.context.SecurityContextHolder;
 import org.springframework.security.core.userdetails.UserDetails;
 import org.springframework.security.core.userdetails.UsernameNotFoundException;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
 import org.springframework.security.oauth2.core.OAuth2AccessToken;
 import org.springframework.security.oauth2.core.user.OAuth2User;
 import org.springframework.stereotype.Service;
 import igeo.site.Model.User;
 import igeo.site.Repository.UserRepository;
 import org.springframework.transaction.annotation.Transactional;
 import org.springframework.ui.Model;
 import org.springframework.web.server.ResponseStatusException;
 import org.springframework.web.servlet.HttpServletBean;
 import org.springframework.web.servlet.view.RedirectView;

 import java.io.IOException;
 import java.io.UnsupportedEncodingException;
 import java.net.URLEncoder;
 import java.nio.file.Files;
 import java.nio.file.Paths;
 import java.util.Collections;
 import java.util.List;


 @Service
 @RequiredArgsConstructor
 public class UserService {
     private final JwtTokenProvider jwtTokenProvider;
     private final UserRepository userRepository;
     private final PasswordEncoder passwordEncoder;
     private final AuthenticationManager authenticationManager;
     // 사용자 저장
     public ResponseEntity<?> save(CreateUserDto createUserDto){

         User user = User.createUser(createUserDto, passwordEncoder);
         User SavedUser = userRepository.save(user);
         return ResponseEntity.ok("User registered successfully with ID: " + SavedUser.getId());
     }
     // 닉네임 중복 확인
     public ResponseEntity<?> checkNickname(String name) {
         System.out.println(name + " 존재여부 : "+userRepository.existsByName(name));
         if (userRepository.existsByName(name)) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 닉네임입니다.");
         }
         return ResponseEntity.ok().body("사용 가능한 닉네임입니다.");
     }
     // 로그인
     public ResponseEntity<?> login(UserLoginDto userLoginDto) {
         try {
             Authentication authentication = authenticationManager.authenticate(
                     new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));
             SecurityContextHolder.getContext().setAuthentication(authentication);
             String token = jwtTokenProvider.generateToken(authentication);
             User user = getUserInfo(userLoginDto.getEmail());
             LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                     .Token(token)
                     .Level(user.getLevel())
                     .Nickname(user.getName())
                     .Character(user.getCharacter())
                     .build();
             return ResponseEntity.ok(loginResponseDto);
         } catch (AuthenticationException e) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
         }
     }
     public RedirectView GoogleLogin(CustumOAuth2User oauth2User, HttpServletResponse response) throws UnsupportedEncodingException
     {
         String registrationId = oauth2User.getRegistrationId();
         // OAuth2 로그인이 성공한 경우
         if ("google".equals(registrationId)) {
             String email = oauth2User.getAttribute("email");
             // JWT 토큰 생성
             Authentication googleAuthentication = new UsernamePasswordAuthenticationToken(email, "N/A");
             String jwtToken = jwtTokenProvider.generateToken(googleAuthentication);
             User user = getUserInfo(email);

             // 로그인 응답 데이터 준비
             String redirectUrl = "http://localhost:3000/login-success"; // 클라이언트 측 URL
             redirectUrl += "?token=" + jwtToken + "&level=" + user.getLevel() + "&nickname=" + URLEncoder.encode(user.getName(), "UTF-8")+"&Character=" + user.getCharacter();

             // 클라이언트로 리다이렉트
             return new RedirectView(redirectUrl);
         }

         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 접근입니다.");
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

     //닉네임 업데이트
    public ResponseEntity<?> updateNickname(String newNickname)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = getUserInfo(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유저가 아닙니다.");
            }
            user.setName(newNickname);
            userRepository.save(user);
            return ResponseEntity.ok().body(user.getName());
        } else {
            // 인증되지 않은 경우에 대한 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지않은 유저입니다.");
        }
    }

     public ResponseEntity<?> updateCharacterNum(String characterNum)
     {
         User user = getLoginUserInfo();
         user.setCharacter(Integer.parseInt(characterNum));
         userRepository.save(user);
         return ResponseEntity.ok().body("성공");
     }


     // 유저 삭제
     public ResponseEntity<?> deleteUserByUsername() {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
             String email = authentication.getName();
             User user = getUserByName(email);
             if (user == null) {
                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not Found  User");
             }
             userRepository.delete(user);
             return ResponseEntity.ok().body(user.getName());
         } else {
             // 인증되지 않은 경우에 대한 처리
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
         }
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
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
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
            } else {
                // 인증되지 않은 경우에 대한 처리
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }
        }
        catch (UsernameNotFoundException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    //인증유저 정보 조회(USER)
    public User getAuthenticatedUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication == null || !authentication.isAuthenticated() ||
                 authentication instanceof AnonymousAuthenticationToken) {
             throw new IllegalArgumentException("로그인이 필요합니다.");
         }

         // OAuth2 로그인을 통한 인증인 경우
         if (authentication.getPrincipal() instanceof OAuth2User) {
             OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
             String email = oauth2User.getAttribute("email");
             return getUserInfo(email);
         }
         // 기타 인증 방식 (예: form 로그인)의 경우
         else {
             return getUserInfo(authentication.getName());
         }
    }
    //인증된 사용자 인지확인
     public ResponseEntity<?> checkAuthentication() {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication == null || !authentication.isAuthenticated() ||
                 authentication instanceof AnonymousAuthenticationToken) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("다시 로그인해주십시요");
         }
         return ResponseEntity.ok(refreshToken());
     }

     //토큰 갱신
    public ResponseEntity<?> refreshToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("다시 로그인해주십시요");
        }
        String token = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(token);
    }

    //EndGameDto정보 조회해서 리턴
     public List<EndOfGameDto> getEndOfGameDtos(String roomId,Room room) {
         //room의 currentUsers를 순회하며 EndOfGameDto를 생성하여 리스트에 추가
        List<EndOfGameDto> endOfGameDtos = room.getCurrentUsers().stream()
                 .map(userId -> {
                     User user = getUserById(userId);
                     return EndOfGameDto.builder()
                             .name(user.getName())
                             .level(user.getLevel())
                             .exp(user.getExp())
                             .nextExp(user.getNextExp())
                             .build();
                 })
                 .toList();
         return endOfGameDtos;
     }

 }
