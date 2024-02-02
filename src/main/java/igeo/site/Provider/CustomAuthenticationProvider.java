package igeo.site.Provider;

import igeo.site.Auth.CustomUserDetailsService;
import igeo.site.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String password = authentication.getCredentials().toString();

        // 사용자 이름을 기반으로 데이터베이스 등에서 사용자 정보를 조회
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(authentication.getName());

        // 비밀번호 암호화된 상태로 비교
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        // 인증 객체를 반환
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
    @Override
    public boolean supports(Class<?> authentication) {
        // 지원하는 인증 객체 타입을 지정
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}







