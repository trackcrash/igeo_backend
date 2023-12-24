 package igeo.site.Service;

 import igeo.site.Config.SpringSecurityConfig;
 import lombok.Getter;
 import lombok.RequiredArgsConstructor;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.core.context.SecurityContextHolder;
 import org.springframework.security.core.userdetails.UserDetails;
 import org.springframework.security.core.userdetails.UserDetailsService;
 import org.springframework.security.core.userdetails.UsernameNotFoundException;
 import org.springframework.stereotype.Service;
 import igeo.site.Model.User;
 import igeo.site.Repository.UserRepository;

 import java.util.List;


 @RequiredArgsConstructor
 @Service
 public class UserService implements UserDetailsService {
     @Getter
     public enum stateCode {
         DUPLICATED_EMAIL(400),
         DUPLICATED_NICKNAME(401),
         PASSWORD_NOT_OK(402),
         NOT_AUTHENTICATED(403),
         UNKNWON_EXCEPTION(404),
         OK(200);
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
         User currentUser = (User) authentication.getDetails();
         return currentUser;
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
         SpringSecurityConfig springSecurityConfig = new SpringSecurityConfig();

         String HashedNewPassword = springSecurityConfig.passwordEncoder().encode(password);

         if (!user.getPassword().equals(HashedNewPassword)) {
             return null;
         }
         String existingPassword = user.getPassword();
         HashedNewPassword = springSecurityConfig.passwordEncoder().encode(newPassword);
         boolean resultBool = HashedNewPassword.equals(existingPassword);
         if(resultBool || newPassword.isBlank()) return user;
         user.setPassword(HashedNewPassword);
         return user;
     }
     public int UserProfileChange(String nickName, String password, String newPassword) {
         try {
             User user = getCurrentUser();
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
            return stateCode.UNKNWON_EXCEPTION.getState();
        }
     }
     public int insertCharacterNumber(int characterNumber)
     {
        if(!isAuthenticated()) return stateCode.NOT_AUTHENTICATED.getState();
        User currentUser = getCurrentUser();
        currentUser.setCharacter(characterNumber);
        return stateCode.OK.getState();
     }

 }
