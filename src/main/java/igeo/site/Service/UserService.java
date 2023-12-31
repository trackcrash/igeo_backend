 package igeo.site.Service;

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
 import java.util.Base64;
 import java.util.Date;
 import java.util.List;


 @RequiredArgsConstructor
 @Service
 public class UserService implements UserDetailsService {

     @Getter
     public enum stateCode {
         DUPLICATED_EMAIL(1001),
         DUPLICATED_NICKNAME(1002),
         PASSWORD_NOT_OK(1003),
         NOT_AUTHENTICATED(1004),
         UNKNOWN_EXCEPTION(1005),
         INVALID_EMAIL(1010),
         INVALID_PASSWORD(1011),
         OK(1000);
         private final int state;
         stateCode(int state) {
             this.state = state;
         }

     }
     @Autowired
     private UserRepository userRepository;
     public User getCurrentUser() {
         // 현재 사용자 정보 가져오기
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         return (User) authentication.getDetails();
     }

     public User save(User user){
         if(checkDuplicateUserEmail(user)) return userRepository.save(user);
         return null;
     }

     public boolean isAuthenticated() {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         return authentication != null && authentication.isAuthenticated();
     }

     private boolean checkDuplicateUserEmail(User user){
         User findUser = userRepository.findByEmail(user.getEmail());
         return findUser == null;
     }
     public boolean checkDuplicateUserNickName(String nickName)
     {
         List<User> findAll = userRepository.findAll();
         for (User user : findAll) {
             if (user.getName().equals(nickName)) return false;
         }
         return true;
     }

     private User userNickNameChange( User user, String nickName)
     {
         if(nickName.isBlank()) return user;
         if(!checkDuplicateUserNickName(nickName)) return null;
         user.setName(nickName);
         return user;
     }
     private User userPasswordChange( User user, String password, String newPassword)
     {
         if (!user.getPassword().equals(password)) {
             return null;
         }
         String existingPassword = user.getPassword();
         boolean resultBool = newPassword.equals(existingPassword);
         if(resultBool || newPassword.isBlank()) return user;

         user.setPassword(newPassword);
         return user;
     }
     public int UserProfileChange(String nickName, String password, String newPassword, PasswordEncoder passwordEncoder) {
         try {
             User user = getCurrentUser();
             password = passwordEncoder.encode(password);
             newPassword = passwordEncoder.encode(newPassword);
             user = userPasswordChange(user, password,newPassword);
             if(user == null)
             {
                System.out.println("기존 비밀번호가 틀렸습니다.");
                return stateCode.PASSWORD_NOT_OK.getState();
             }
             user = userNickNameChange(user, nickName);
             if(user == null)
             {
                System.out.println("다른 유저가 이용중인 닉네임입니다.");
                return stateCode.DUPLICATED_NICKNAME.getState();
             }
             userRepository.save(user);
         } catch (Exception ex) {
             System.out.println(ex);
         }
         return stateCode.OK.getState();
     }

     @Override
     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
         User user = userRepository.findByEmail(email);
         if (user == null){
             throw  new UsernameNotFoundException(email);
         }
         return   org.springframework.security.core.userdetails.User.builder()
                 .username(user.getEmail())
                 .password(user.getPassword())
                 .authorities(user.getPermissions())
                 .build();
     }


     public int deleteAccount()
     {
        if(!isAuthenticated()) return stateCode.NOT_AUTHENTICATED.getState();
        try
        {
            User currentUser = getCurrentUser();
            userRepository.delete(currentUser);
            return stateCode.OK.getState();
        }catch(Exception ex)
        {
            System.out.printf("An error occurred while deleting the account: %s%n", ex);
            return stateCode.UNKNOWN_EXCEPTION.getState();
        }
     }
     public int insertCharacterNumber(int characterNumber)
     {
        if(!isAuthenticated()) return stateCode.NOT_AUTHENTICATED.getState();
        User currentUser = getCurrentUser();
        currentUser.setCharacter(characterNumber);
        return stateCode.OK.getState();
     }
     public String login(String email, String password, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
         try {
             UserDetails userDetails = loadUserByUsername(email);

             Authentication authentication = new UsernamePasswordAuthenticationToken(
                     userDetails, password, userDetails.getAuthorities());
             authenticationManager.authenticate(authentication);

             String token = generateToken(userDetails, KeyGenerator.generateSecretKey(64), 60 * 60 * 1000);

             return token;
         } catch (BadCredentialsException e) {
             return Integer.toString(stateCode.INVALID_PASSWORD.getState());
         } catch (UsernameNotFoundException e) {
             return Integer.toString(stateCode.INVALID_EMAIL.getState());
         } catch (Exception e) {
             return Integer.toString(stateCode.UNKNOWN_EXCEPTION.getState());
         }
     }
     private String generateToken(UserDetails userDetails, String secretKey, long expiration) {
         return Jwts.builder()
                 .setSubject(userDetails.getUsername())
                 .setIssuedAt(new Date())
                 .setExpiration(new Date(System.currentTimeMillis() + expiration))
                 .signWith(SignatureAlgorithm.HS256, secretKey)
                 .compact();
     }
     public class KeyGenerator {
         public static String generateSecretKey(int length) {
             SecureRandom secureRandom = new SecureRandom();
             byte[] keyBytes = new byte[length];
             secureRandom.nextBytes(keyBytes);
             return Base64.getEncoder().encodeToString(keyBytes);
         }
     }

 }

