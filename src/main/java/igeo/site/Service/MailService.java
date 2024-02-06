package igeo.site.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@EnableScheduling
public class MailService {

    private final JavaMailSender emailSender;
    private final Map<String, Integer> emailVerificationCodes = new HashMap<>();
    public ResponseEntity<?> sendEmail(String toEmail) {
        int genCode = generateEmailCode();
        String title = "Igeo 이메일 인증 코드";
        String text = "아이거에서 보낸 이메일 인증 코드입니다,\n\n";
        text += "인증코드 :" + genCode +"\n\n";

        text += "이 인증 코드를 웹사이트에 입력하여 이메일 인증을 완료해주세요.\n\n";


        text += "감사합니다.";
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
        try {
            emailSender.send(emailForm);
            emailVerificationCodes.put(toEmail, genCode);
            return ResponseEntity.ok().body("인증번호 전송 완료");
        } catch (MessagingException e) {
            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, text);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("인증번호 전송 실패");
        }
    }
    public ResponseEntity<?> verifyEmail(String toEmail, int providedCode) {
        if (emailVerificationCodes.containsKey(toEmail)) {
            int storedCode = emailVerificationCodes.get(toEmail);
            if (storedCode == providedCode) {
                emailVerificationCodes.remove(toEmail); // Remove used code
                return ResponseEntity.ok().body("이메일 인증 성공");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 인증번호입니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 인증 요청이 없거나 만료되었습니다.");
        }
    }
    private int generateEmailCode()
    {
        Random random = new Random();
        return random.nextInt(1000,9999);
    }

    // 발신할 이메일 데이터 세팅
    private SimpleMailMessage createEmailForm(String toEmail,
                                              String title,
                                              String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }

    @Scheduled(fixedRate = 300000) // 5분(300,000밀리초)마다 실행
    public void clearExpiredVerificationCodes() {
        long currentTime = System.currentTimeMillis();
        emailVerificationCodes.entrySet().removeIf(entry -> entry.getValue() < currentTime);
    }
}
