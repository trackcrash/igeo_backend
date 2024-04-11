 package igeo.site.Controller;

 import igeo.site.DTO.NicknameDTO;
 import igeo.site.DTO.UpdateProfileDto;
 import igeo.site.DTO.UserLoginDto;
 import igeo.site.Model.User;
 import lombok.RequiredArgsConstructor;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.security.core.annotation.AuthenticationPrincipal;
 import org.springframework.security.oauth2.core.user.OAuth2User;
 import org.springframework.validation.BindingResult;
 import org.springframework.web.bind.annotation.*;
 import igeo.site.DTO.CreateUserDto;
 import igeo.site.Service.UserService;

 import jakarta.validation.Valid;

 import java.util.Objects;

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
     public User checkAuthentication() {
         return userService.getAuthenticatedUserInfo();
     }

     // 닉네임 중복 확인
     @GetMapping("/check_nickname")
     public ResponseEntity<?> checkNickname(@Valid @ModelAttribute NicknameDTO nicknameDTO, BindingResult bindingResult) {
         if (bindingResult.hasErrors()) {
             return ResponseEntity.badRequest().body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
         }
         return userService.checkNickname(nicknameDTO.getName());
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
