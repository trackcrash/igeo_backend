package igeo.site.Controller;

import igeo.site.DTO.LoginResponseDto;
import igeo.site.Model.CustumOAuth2User;
import igeo.site.Model.User;
import igeo.site.Provider.JwtTokenProvider;
import igeo.site.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
    public RedirectView loginSuccess(@AuthenticationPrincipal  CustumOAuth2User oauth2User, HttpServletResponse response) throws UnsupportedEncodingException {
        // 로그인 성공 후 처리
        String registrationId = oauth2User.getRegistrationId();
        // OAuth2 로그인이 성공한 경우
        if ("google".equals(registrationId)) {
            String email = oauth2User.getAttribute("email");
            // JWT 토큰 생성
            Authentication googleAuthentication = new UsernamePasswordAuthenticationToken(email, null);
            String jwtToken = jwtTokenProvider.generateToken(googleAuthentication);
            User user = userService.getUserInfo(email);

            // 로그인 응답 데이터 준비
            String redirectUrl = "http://localhost:3000/login-success"; // 클라이언트 측 URL
            redirectUrl += "?token=" + jwtToken + "&level=" + user.getLevel() + "&nickname=" + URLEncoder.encode(user.getName(), "UTF-8");

            // 클라이언트로 리다이렉트
            return new RedirectView(redirectUrl);
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 접근입니다.");
    }
//    @GetMapping("/login/oauth2/code/google")
//    public String googleCallback(@AuthenticationPrincipal OAuth2User oauth2User,
//                                 OAuth2AuthenticationToken authentication) {
//
//        return null;
//    }
}