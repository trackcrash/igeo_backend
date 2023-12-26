package igeo.site.Service;

import igeo.site.DTO.MusicDto;
import igeo.site.Model.Mission;
import igeo.site.Model.Music;
import igeo.site.Repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicService {

    @Autowired
    private MusicRepository musicRepository;

    //missionId로 음악 리스트 가져오기
    //결과값 : 음악 리스트
    public List<Music> getMusicByMission(Long id) {
        return musicRepository.findByMission_Id(id);
    }

    //List에서 하나씩 꺼내서 MusicDto로 변환
    public MusicDto transferMusicData(List<Music> musicList, int index) {
        Music music = musicList.get(index);
        return MusicDto.builder()
                .title(music.getTitle())
                .song(music.getSong())
                .youtube_url(music.getYoutube_url())
                .thumbnail_url(music.getThumbnail_url())
                .answer(music.getAnswer())
                .hint(music.getHint())
                .startTime(music.getStartTime())
                .endTime(music.getEndTime())
                .category(music.getCategory())
                .build();
    }

    public Music getMusicById(Long id) {
        return musicRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 음악입니다.")
        );
    }

    public Music save(Music music) {
        return musicRepository.save(music);
    }

}
