 package igeo.site.Controller;

 import igeo.site.DTO.UserLoginDto;
 import igeo.site.Model.User;
 import lombok.RequiredArgsConstructor;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.security.authentication.AnonymousAuthenticationToken;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.core.context.SecurityContextHolder;
 import org.springframework.validation.BindingResult;
 import org.springframework.web.bind.annotation.*;
 import igeo.site.DTO.CreateUserDto;
 import igeo.site.Service.UserService;

 import jakarta.validation.Valid;

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
         if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
             // 현재 사용자가 인증되었음
             return ResponseEntity.ok("Authenticated user: " + authentication.getName());
         } else {
             // 현재 사용자가 인증되지 않았음
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
         }
     }

     // 회원 가입
     @PostMapping("/register")
     public ResponseEntity<?> register(@Valid @RequestBody CreateUserDto createUserDto, BindingResult bindingResult) {
         if (bindingResult.hasErrors()) {
             return ResponseEntity.badRequest().body("Invalid request.");
         }
         User user = userService.save(createUserDto);
         return ResponseEntity.ok().body("User registered successfully with ID: " + user.getId());
     }

     // 계정 삭제 확인 페이지
     @GetMapping("/delete_account_confirm")
     public ResponseEntity<?> deleteAccountConfirm() {
         return ResponseEntity.ok().body("Please confirm your account deletion.");
     }

     // 계정 삭제
     @DeleteMapping("/delete_account")
     public ResponseEntity<?> deleteAccount() {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
             String email = authentication.getName();
             String result = userService.deleteUserByUsername(email);
             return ResponseEntity.ok().body(result);
         } else {
             // 인증되지 않은 경우에 대한 처리
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
         }
     }
 }
