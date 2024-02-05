package igeo.site.Controller;

import igeo.site.Model.CustumOAuth2User;
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
        // 로그인 성공 후 처리
        String registrationId = oauth2User.getRegistrationId();
        // OAuth2 로그인이 성공한 경우
        if ("google".equals(registrationId)) {
            String email = oauth2User.getAttribute("email");

            // 여기에서 추가적인 로직 수행 가능

            // JWT 토큰 생성
            Authentication googleAuthentication = new UsernamePasswordAuthenticationToken(email, null);
            String jwtToken = jwtTokenProvider.generateToken(googleAuthentication);
            System.out.println(jwtToken);
            return ResponseEntity.ok(jwtToken);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 접근입니다.");
    }
//    @GetMapping("/login/oauth2/code/google")
//    public String googleCallback(@AuthenticationPrincipal OAuth2User oauth2User,
//                                 OAuth2AuthenticationToken authentication) {
//
//        return null;
//    }
}