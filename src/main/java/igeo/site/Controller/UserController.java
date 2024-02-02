 package igeo.site.Controller;

 import igeo.site.DTO.TokenRequestDto;
 import igeo.site.DTO.UserLoginDto;
 import io.swagger.v3.oas.annotations.Operation;
 import io.swagger.v3.oas.annotations.Parameter;
 import io.swagger.v3.oas.annotations.enums.ParameterIn;
 import lombok.RequiredArgsConstructor;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.RequestEntity;
 import org.springframework.http.ResponseEntity;
 import org.springframework.security.access.prepost.PreAuthorize;
 import org.springframework.security.authentication.AnonymousAuthenticationToken;
 import org.springframework.security.authentication.AuthenticationManager;
 import org.springframework.security.authentication.BadCredentialsException;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.core.context.SecurityContext;
 import org.springframework.security.core.context.SecurityContextHolder;
 import org.springframework.security.core.userdetails.UserDetails;
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
             return userService.login(userLoginDto, authenticationManager);
         }catch(UsernameNotFoundException e)
         {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
         }
         catch (BadCredentialsException e) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password.");
         } catch (Exception e) {
             System.out.println(e.getMessage());
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing your request.");
         }
     }

     @GetMapping("/check_authentication")
     public ResponseEntity<String> checkAuthentication() {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         System.out.println("checkAuthentication"+authentication);
         if (authentication != null && authentication.isAuthenticated()) {
             // 현재 사용자가 인증되었음
             return ResponseEntity.ok("Authenticated user: " + authentication.getName());
         } else {
             // 현재 사용자가 인증되지 않았음
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
         }
     }
//    @PostMapping("/TokenTest")
//    public ResponseEntity<?> tokenTest(@Valid @RequestBody TokenRequestDto tokenRequestDto)
//    {   try
//        {
//            return userService.handleClientRequest(tokenRequestDto.getToken(), tokenRequestDto.getEmail());
//        }
//        catch(UsernameNotFoundException e)
//        {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
//        }
//    }

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
     public String  DeleteAccountConfirm()
     {
        return "user/DeleteAccountConfirm";
     }

     @GetMapping("/delete_account")
     public String DeleteAccount()
     {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         System.out.println(authentication.getPrincipal());
         if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
             UserDetails userDetails = userService.getCurrentUser(authentication);

             if (userDetails != null) {
                 return userDetails.getUsername();
             } else {
                 // 사용자 정보를 찾지 못한 경우에 대한 처리
                 return "User details not found";
             }
         } else {
             // 인증되지 않은 경우에 대한 처리
             return "User not authenticated";
         }
     }
 }
