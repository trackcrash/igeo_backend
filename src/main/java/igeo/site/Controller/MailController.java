package igeo.site.Controller;

import igeo.site.Service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @ResponseBody
    @PostMapping("/api/send_verification_email")
    public ResponseEntity<?> mailSend(String mail){
        return mailService.sendEmail(mail);
    }
    @ResponseBody
    @PostMapping("/api/verify_verification_code")
    public ResponseEntity<?> verifyVerificationCode(String mail,int code){
        return mailService.verifyEmail(mail,code);
    }
}
