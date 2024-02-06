package igeo.site.Controller;

import igeo.site.Service.NameCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NameCheckController {
    private final NameCheckService nameCheckService;

    @GetMapping("/api/name_check")
    public ResponseEntity<String> nameCheck(@RequestParam String name)
    {
        if(!nameCheckService.nameDuplicationCheck(name)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("닉네임이 중복되어 있습니다.");
        if(!nameCheckService.nameBannedCheck(name)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("닉네임에 금지된 단어가 포함 되어있습니다..");
        return ResponseEntity.ok().body("Success");
    }
}
