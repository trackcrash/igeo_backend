package igeo.site.Controller;

import igeo.site.DTO.LoginResponseDto;
import igeo.site.Model.CustumOAuth2User;
import igeo.site.Model.User;
import igeo.site.Provider.JwtTokenProvider;
import igeo.site.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class GoogleLoginController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/login/google")
    public String googleLogin() {
        return "redirect:/oauth2/authorization/google";
    }
    @GetMapping("/login-success")
    public ResponseEntity<?> loginSuccess(@AuthenticationPrincipal CustumOAuth2User oauth2User, Model model) {
        return userService.GoogleLogin(oauth2User, model);
    }
//    @GetMapping("/login/oauth2/code/google")
//    public String googleCallback(@AuthenticationPrincipal OAuth2User oauth2User,
//                                 OAuth2AuthenticationToken authentication) {
//
//        return null;
//    }
}