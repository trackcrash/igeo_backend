 package igeo.site.Controller;

 import com.nimbusds.jose.shaded.gson.Gson;
 import lombok.RequiredArgsConstructor;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.security.access.prepost.PreAuthorize;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.ui.Model;
 import org.springframework.validation.BindingResult;
 import org.springframework.web.bind.annotation.*;
 import igeo.site.DTO.CreateUserDto;
 import igeo.site.Model.User;
 import igeo.site.Service.UserService;

 import jakarta.validation.Valid;
 import java.util.*;

 @RequiredArgsConstructor
 @RestController
 @RequestMapping("/user")
 public class UserController {

     @Autowired
     private UserService userService;

     @Autowired
     private PasswordEncoder passwordEncoder;

     //로그인
     @GetMapping ("/login")
     public String login(){
     Map<String, Object> data = new HashMap<>();
     int state=UserService.stateCode.OK.getState();
     data.put("state", state);
     Gson gson = new Gson();
     String json = gson.toJson(data);
     return json;
     }
     @PostMapping("login")
     public String handleLoginPostRequest(@RequestBody Map<String,String> map)
     {
        String email = map.get("email");
        String password = map.get("password");
        //로그인 로직
        return "";
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
         User user = User.createUser(createUserDto, passwordEncoder);
         if(userService.save(user) == null)
         {
             model.addAttribute("errorMessage");
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
