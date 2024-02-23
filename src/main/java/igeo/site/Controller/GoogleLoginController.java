package igeo.site.Controller;

import igeo.site.Model.CustumOAuth2User;
import igeo.site.Provider.JwtTokenProvider;
import igeo.site.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequiredArgsConstructor
public class GoogleLoginController {
    private final UserService userService;

    @GetMapping("/login/google")
    public String googleLogin() {
        return "redirect:/oauth2/authorization/google";
    }
    @GetMapping("/login-success")
    public RedirectView loginSuccess(@AuthenticationPrincipal  CustumOAuth2User oauth2User, HttpServletResponse response) {
        try
        {
            return userService.GoogleLogin(oauth2User, response);
        }
        catch(Exception e)
        {
            return new RedirectView("http://localhost:3000");
        }
        // 로그인 성공 후 처리
    }
}