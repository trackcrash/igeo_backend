 package igeo.site.Controller;

 import igeo.site.DTO.UserLoginDto;
 import lombok.RequiredArgsConstructor;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.security.access.prepost.PreAuthorize;
 import org.springframework.security.authentication.AuthenticationManager;
 import org.springframework.security.authentication.BadCredentialsException;
 import org.springframework.security.core.userdetails.UsernameNotFoundException;
 import org.springframework.validation.BindingResult;
 import org.springframework.web.bind.annotation.*;
 import igeo.site.DTO.CreateUserDto;
 import igeo.site.Service.UserService;

 import jakarta.validation.Valid;

 @RestController
 @RequiredArgsConstructor
 @RequestMapping("/user")
 public class UserController {

     @Autowired
     private UserService userService;
     @Autowired
     private AuthenticationManager authenticationManager;

     //로그인
     @PostMapping("/login")
     public ResponseEntity<?> handleLoginPostRequest(@Valid @RequestBody UserLoginDto userLoginDto, BindingResult bindingResult) {
         if (bindingResult.hasErrors()) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request.");
         }
         try {
             return userService.Login(userLoginDto, authenticationManager); // JWT 토큰 반환
         }catch(UsernameNotFoundException e)
         {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
         }
         catch (BadCredentialsException e) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password.");
         } catch (Exception e) {
             System.out.println(e);
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing your request.");
         }
     }



     @PostMapping("/register")
     public ResponseEntity<?> register(@Valid @RequestBody CreateUserDto createUserDto, BindingResult bindingResult){
         if(bindingResult.hasErrors()){
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request.");
         }
         try{
             userService.save(createUserDto);
             return ResponseEntity.ok().body("User registered successfully!");
         }catch (Exception e){
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing your request.");
         }
     }

     @GetMapping("/delete_account_confirm")
     @PreAuthorize("isAuthenticated()")
     public String  DeleteAccountConfirm()
     {
        return "user/DeleteAccountConfirm";
     }

     @GetMapping("/delete_account")
     public String DeleteAccount()
     {
        return "";
     }
 }
