package igeo.site.Controller;

import igeo.site.DTO.MusicDto;
import igeo.site.Game.MissionTracker;
import igeo.site.Model.Music;
import igeo.site.Service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/music")
public class musicController {

    @Autowired
    private MusicService musicService;

    //음악관리를 위한 tracker 사용
    private MissionTracker tracker = new MissionTracker();

    //게임 시작시 음악 리스트 가져오기
    //결과값 : 첫번 째 음악
    @GetMapping("/{roomId}/{missionId}")
    public ResponseEntity<MusicDto> startMission(@PathVariable Long roomId, @PathVariable Long missionId) {
        List<Music> missionList = musicService.getMusicByMission(missionId);
        //음악 리스트 섞기
        tracker.shuffleMusic(missionList);
        //음악 리스트 저장(방번호, 음악리스트)
        tracker.setMusicList(roomId, missionList);
        tracker.setCurrentMusic(roomId, 0);
        if (!missionList.isEmpty()) {
            MusicDto musicDto = musicService.transferMusicData(missionList, 0);
            return ResponseEntity.ok(musicDto);
        }
        //에러 발생시
        return ResponseEntity.ok(null);
    }

    // 다음 음악으로 넘어가기
    @GetMapping("/{roomId}/next")
    public ResponseEntity<MusicDto> getNextMusic(@PathVariable Long roomId) {
        tracker.nextMusic(roomId);
        //음악 리스트 가져오기
        List<Music> missionList = tracker.getMusicList(roomId);
        //현재 음악 인덱스 가져오기
        int currentIndex = tracker.getCurrentMusicIndex(roomId);
        //정상 진행
        if (currentIndex >= 0 && currentIndex < missionList.size()) {
            MusicDto musicDto = musicService.transferMusicData(missionList, currentIndex);
            return ResponseEntity.ok(musicDto);
        }
        //에러 발생시
        return ResponseEntity.ok(null);
    }
}
