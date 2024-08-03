package igeo.site.Auth;

import igeo.site.DTO.CreateUserDto;
import igeo.site.Model.CustomOAuth2User;
import igeo.site.Model.User;
import igeo.site.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        // 회원 가입 여부 체크 및 처리
        processOAuth2User(oauth2User);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        return new CustomOAuth2User(oauth2User,registrationId);
    }

    private void processOAuth2User(OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        System.out.println("Received email from OAuth2 provider: " + email);

        if (!userRepository.existsByEmail(email)) {
            System.out.println("User does not exist, creating a new user...");

            // 회원 가입 로직
            CreateUserDto createUserDto  = new CreateUserDto();
            createUserDto.setEmail(oauth2User.getAttribute("email"));
            createUserDto.setName(oauth2User.getAttribute("name"));
            createUserDto.setPassword("N/A");
            User newUser = User.createGoogleUser(createUserDto);
            userRepository.save(newUser);

            System.out.println("New user created successfully.");
        } else {
            System.out.println("User already exists.");
        }

    }
}