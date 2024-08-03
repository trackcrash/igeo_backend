package igeo.site.Controller;

import igeo.site.Model.CustomOAuth2User;
import igeo.site.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class GoogleLoginController {
    private final UserService userService;

    @GetMapping("/login/google")
    public String googleLogin() {
        return "redirect:/oauth2/authorization/google";
    }
    @GetMapping("/login-success")
    public RedirectView loginSuccess(@RequestParam String token, @RequestParam String refreshToken, @RequestParam String nickname, @RequestParam String level, @RequestParam String character) {
        try {
            return userService.googleLogin(token, refreshToken, nickname, level, character);
        } catch (Exception e) {
            return new RedirectView("http://localhost:3000");
        }
    }
}