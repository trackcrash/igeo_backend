package igeo.site.Controller;

import igeo.site.DTO.ImageDto;
import igeo.site.DTO.MusicDto;
import igeo.site.Service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/image")
public class ImageController {
    private final ImageService imageService;

    //게임 시작시 음악 리스트 가져오기
    //결과값 : 첫번 째 음악
    @GetMapping("/{roomId}/{missionId}")
    public ResponseEntity<ImageDto> startMission(@PathVariable Long roomId, @PathVariable Long missionId) {
        ImageDto imageDto = imageService.startMission(roomId, missionId);
        return imageDto != null ? ResponseEntity.ok(imageDto) : ResponseEntity.notFound().build();
    }

    // 다음 음악으로 넘어가기
    @GetMapping("/{roomId}/next")
    public ResponseEntity<ImageDto> getNextImage(@PathVariable Long roomId) {
        //TODO: 권한 인증(방장만,스킵인원수가 차지않아도 넘어가지않게)
        ImageDto imageDto = imageService.getNextImage(roomId);
        return imageDto != null ? ResponseEntity.ok(imageDto) : ResponseEntity.notFound().build();
    }
}
