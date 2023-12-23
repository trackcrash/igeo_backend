 package igeo.site.Service;

 import lombok.RequiredArgsConstructor;
 import org.apache.logging.log4j.message.Message;
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
     @Autowired
     private UserRepository userRepository;

     public User getCurrentUser() {
         // 현재 사용자 정보 가져오기
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         User currentUser = (User) authentication.getDetails();

         return currentUser;
     }
     public User save(User user){
         checkDuplicateUser(user);
         return userRepository.save(user);
     }
     public boolean isAuthenticated() {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         return authentication != null && authentication.isAuthenticated();
     }

     private void checkDuplicateUser(User user){
         User findUser = userRepository.findByEmail(user.getEmail());
         if (findUser != null){
             throw new IllegalStateException("이미 가입된 아이디입니다");
         }
     }
     public void checkDuplicateUserNickName(String nickName)
     {
         List<User> findAll = userRepository.findAll();
         if(nickName.isBlank())
         {
            throw new IllegalStateException("공백 닉네임은 이용이 불가능 합니다.");
         }
         findAll.forEach(user->
         {
            if(user.getName().equals(nickName))
            {
                throw new IllegalStateException("다른 사용자가 이용중인 닉네임 입니다.");
            }
         });
     }

     public void UserProfileChange(String nickName, String password, String newPassword)
     {
        if(isAuthenticated())
        {
            try
            {
                User user = getCurrentUser();
                checkDuplicateUserNickName(nickName);
                user.setName(nickName);
                userRepository.save(user);
            }catch (IllegalStateException ex)
            {
                System.out.println(ex);
            }
        }
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


     public boolean deleteAccount()
     {
         if(isAuthenticated())
         {
            try
            {
                User currentUser = getCurrentUser();
                userRepository.delete(currentUser);
                return true;
            }catch(Exception ex)
            {
                System.out.printf("An error occurred while deleting the account: %s%n", ex);
                return false;
            }
         }
         return false;
     }
 }
