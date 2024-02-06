package igeo.site.Controller;

import igeo.site.DTO.AnswerDto;
import igeo.site.DTO.MusicDto;
import igeo.site.Service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/music")
public class MusicController {

    private final MusicService musicService;

    //게임 시작시 음악 리스트 가져오기
    //결과값 : 첫번 째 음악
    @GetMapping("/{roomId}/{missionId}")
    public ResponseEntity<MusicDto> startMission(@PathVariable Long roomId, @PathVariable Long missionId) {
        MusicDto musicDto = musicService.startMission(roomId, missionId);
        return musicDto != null ? ResponseEntity.ok(musicDto) : ResponseEntity.notFound().build();
    }

    // 다음 음악으로 넘어가기
    @GetMapping("/{roomId}/next")
    public ResponseEntity<MusicDto> getNextMusic(@PathVariable Long roomId) {
        //TODO: 권한 인증(방장만,스킵인원수가 차지않아도 넘어가지않게)
        MusicDto musicDto = musicService.getNextMusic(roomId);
        return musicDto != null ? ResponseEntity.ok(musicDto) : ResponseEntity.notFound().build();
    }

}
