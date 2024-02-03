package igeo.site.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GoogleLoginController {

    @GetMapping("/login/google")
    public String googleLogin() {
        // Redirect to Google OAuth2 login page
        return "redirect:/oauth2/authorization/google";
    }

    @GetMapping("/login/google/callback")
    public String googleCallback() {
        // Handle Google OAuth2 callback
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get user details from authentication
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        // You can now use the email or other user details to save the user or perform other actions

        return "redirect:/index";
    }
}
