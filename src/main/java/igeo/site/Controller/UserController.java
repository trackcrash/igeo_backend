 package igeo.site.Controller;

 import igeo.site.DTO.UpdateProfileDto;
 import igeo.site.DTO.UserLoginDto;
 import igeo.site.Model.User;
 import lombok.RequiredArgsConstructor;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.core.io.ClassPathResource;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.security.authentication.AnonymousAuthenticationToken;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.core.annotation.AuthenticationPrincipal;
 import org.springframework.security.core.context.SecurityContextHolder;
 import org.springframework.security.core.userdetails.UsernameNotFoundException;
 import org.springframework.security.oauth2.core.user.OAuth2User;
 import org.springframework.ui.Model;
 import org.springframework.validation.BindingResult;
 import org.springframework.web.bind.annotation.*;
 import igeo.site.DTO.CreateUserDto;
 import igeo.site.Service.UserService;

 import jakarta.validation.Valid;

 import java.io.IOException;
 import java.nio.file.Files;
 import java.nio.file.Paths;
 import java.util.Collections;
 import java.util.Dictionary;
 import java.util.List;

 @RestController
 @RequiredArgsConstructor
 @RequestMapping("/user")
 public class UserController {

     private final UserService userService;
     // 로그인
     @PostMapping("/login")
     public ResponseEntity<?> handleLoginPostRequest(@Valid @RequestBody UserLoginDto userLoginDto, BindingResult bindingResult) {
         if (bindingResult.hasErrors()) {
             return ResponseEntity.badRequest().body("Invalid request.");
         }
         return userService.login(userLoginDto);
     }
     // 인증 확인
     @GetMapping("/check_authentication")
     public ResponseEntity<String> checkAuthentication() {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         System.out.println(authentication);
         if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
             // 현재 사용자가 인증되었음
             return ResponseEntity.ok("Authenticated user: " + authentication.getName());
         } else {
             // 현재 사용자가 인증되지 않았음
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
         }
     }
    // 구글 인증 확인 - 캬루
     //구글인증의경우 받아올때 OAuth2User로 받아옴
     @GetMapping("/google_authentication_check")
        public ResponseEntity<String> googleAuthenticationCheck(@AuthenticationPrincipal OAuth2User oauth2User) {
            if (oauth2User != null) {
                return ResponseEntity.ok("Authenticated user: " + oauth2User.getAttribute("email"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }
        }

     // 회원 가입
     @PostMapping("/register")
     public ResponseEntity<?> register(@Valid @RequestBody CreateUserDto createUserDto, BindingResult bindingResult) {

         if (bindingResult.hasErrors()) {
             return ResponseEntity.badRequest().body("Invalid request.");
         }
         return userService.save(createUserDto);
     }


     // 계정 삭제 확인 페이지
     @GetMapping("/delete_account_confirm")
     public ResponseEntity<?> deleteAccountConfirm() {
         return ResponseEntity.ok().body("Please confirm your account deletion.");
     }
     // 계정 삭제
     @DeleteMapping("/delete_account")
     public ResponseEntity<?> deleteAccount() {
         return userService.deleteUserByUsername();
     }
     @PutMapping("/update_profile")
     public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileDto updateProfileDto)
     {
        return userService.updateProfile(updateProfileDto);
     }

 }
