package igeo.site.Controller;

import igeo.site.DTO.MusicDto;
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

    @GetMapping("/{roomId}/{missionId}")
    public ResponseEntity<MusicDto> StartMission(@PathVariable Long roomId, @PathVariable Long missionId) {
        List<Music> MissionList = musicService.getMusicByMission(missionId);
        MusicDto MusicDto = musicService.transferMusicData(MissionList, 0);
        return ResponseEntity.ok(MusicDto);
    }

    @GetMapping("next")
    public String getNextMusic(Long id){
        return musicService.getMusicById(id).toString();
    }
}
