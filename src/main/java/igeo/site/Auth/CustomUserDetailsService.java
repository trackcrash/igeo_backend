package igeo.site.Auth;

import igeo.site.Model.User;
import igeo.site.Repository.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

//서비스레이어에 묶여있던 유저디테일서비스 관련 클래스를 따로 분리하여 작성
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Getter
    public enum UserPermission {
        ROLE_USER("ROLE_USER"),
        ROLE_SUPERUSER("ROLE_SUPERUSER"),
        ROLE_ADMIN("ROLE_ADMIN");

        private final String role;

        UserPermission(String role) {
            this.role = role;
        }

        public static UserPermission getByIndex(int index) {
            if (index < 0 || index >= values().length) {
                throw new IllegalArgumentException("Invalid index for UserPermission enum");
            }
            return values()[index];
        }
    }

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //이메일기반 유저정보를 조회하는 메소드
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        UserPermission userPermission = UserPermission.getByIndex(user.getPermissions());
        String roleName = userPermission.getRole();
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(roleName)));
    }
}
