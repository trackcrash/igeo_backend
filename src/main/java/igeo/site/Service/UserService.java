 package igeo.site.Service;

 import lombok.RequiredArgsConstructor;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.security.core.userdetails.UserDetails;
 import org.springframework.security.core.userdetails.UserDetailsService;
 import org.springframework.security.core.userdetails.UsernameNotFoundException;
 import org.springframework.stereotype.Service;
 import igeo.site.Model.User;
 import igeo.site.Repository.UserRepository;

 @RequiredArgsConstructor
 @Service
 public class UserService implements UserDetailsService {
     @Autowired
     private UserRepository userRepository;

     public User save(User user){
         checkDuplicateUser(user);
         return userRepository.save(user);
     }

     private void checkDuplicateUser(User user){
         User findUser = userRepository.findByEmail(user.getEmail());
         if (findUser != null){
             throw new IllegalStateException("이미 가입된 아이디입니다");
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
 }
