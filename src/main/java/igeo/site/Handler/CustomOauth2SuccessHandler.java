package igeo.site.Handler;

import igeo.site.Model.CustomOAuth2User;
import igeo.site.Model.User;
import igeo.site.Provider.JwtTokenProvider;
import igeo.site.Repository.UserRepository;
import igeo.site.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomOauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        User user = userRepository.findByEmail(email);
        String token = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
//        String redirectUrl = "https://igeo.site/login/success?token=" + token + "&refreshToken=" + refreshToken  + "&nickname=" + URLEncoder.encode(name, StandardCharsets.UTF_8);
        String redirectUrl = "http://localhost:8080/login-success?token=" + token + "&refreshToken=" + refreshToken  + "&level=" + user.getLevel() + "&nickname=" + URLEncoder.encode(user.getName(), "UTF-8")+"&Character=" + user.getCharacter();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
