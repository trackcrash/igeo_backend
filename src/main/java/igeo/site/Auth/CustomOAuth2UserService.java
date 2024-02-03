package igeo.site.Auth;

import igeo.site.DTO.CreateUserDto;
import igeo.site.Model.User;
import igeo.site.Repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        // 회원 가입 여부 체크 및 처리
        processOAuth2User(oauth2User);
        return oauth2User;
    }

    private void processOAuth2User(OAuth2User oauth2User) {
        // 회원 가입 여부 체크
        String email = oauth2User.getAttribute("email");
        if (!userRepository.existsByEmail(email)) {
            // 회원 가입 로직
            CreateUserDto createUserDto  = new CreateUserDto();
            createUserDto.setEmail(oauth2User.getAttribute("email"));
            createUserDto.setName(oauth2User.getAttribute("name"));
            createUserDto.setPassword(null);
            User newUser = User.createGoogleUser(createUserDto);
            // ... 기타 필요한 정보 설정
            userRepository.save(newUser);
        }
    }
}