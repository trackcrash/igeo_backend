 package igeo.site.Controller;

 import lombok.RequiredArgsConstructor;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.security.access.prepost.PreAuthorize;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.stereotype.Controller;
 import org.springframework.ui.Model;
 import org.springframework.validation.BindingResult;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.PostMapping;
 import org.springframework.web.bind.annotation.RequestMapping;
 import igeo.site.DTO.CreateUserDto;
 import igeo.site.Model.User;
 import igeo.site.Service.UserService;

 import javax.validation.Valid;

 @RequiredArgsConstructor
 @Controller
 @RequestMapping("/user")
 public class userController {

     @Autowired
     private UserService userService;

     @Autowired
     private PasswordEncoder passwordEncoder;

     //로그인
     @GetMapping ("/login")
     public String login(){
         return "user/login";
     }

     //회원가입
     @GetMapping("/register")
     public String register(Model model){
         model.addAttribute("CreateUserDto",new CreateUserDto());
         return "user/register";
     }

     @PostMapping("/register")
     public String register(@Valid CreateUserDto createUserDto, BindingResult bindingResult, Model model){
         if (bindingResult.hasErrors()){
             return "user/register";
         }
         try {
             User user = User.createUser(createUserDto, passwordEncoder);
             userService.save(user);
         } catch (IllegalStateException e){
             model.addAttribute("errorMessage", e.getMessage());
             return "user/register";
         }
         return "redirect:/";
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
