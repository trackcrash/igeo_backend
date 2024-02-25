package igeo.site.Service;

import igeo.site.DTO.AnswerDto;
import igeo.site.DTO.MusicDto;
import igeo.site.Game.MissionTracker;
import igeo.site.Model.Answer;
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

    private final MusicRepository musicRepository;

    //음악관리를 위한 tracker 사용
    private final MissionTracker tracker;

    //missionId로 음악 리스트 가져오기
    //결과값 : 음악 리스트
    public List<Music> getMusicByMission(Long id) {
        return musicRepository.findByMission_Id(id);
    }
    //List에서 하나씩 꺼내서 MusicDto로 변환
    public MusicDto transferMusicData(List<Music> musicList, int index) {
        Music music = musicList.get(index);
        return MusicDto.builder()
                .id(music.getId())
                .title(music.getTitle())
                .song(music.getSong())
                .youtube_url(music.getYoutube_url())
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

    //음악 저장
    public Music save(MusicDto musicDto, Mission mission) {
        Music music = Music.builder()
                .title(musicDto.getTitle())
                .song(musicDto.getSong())
                .youtube_url(musicDto.getYoutube_url())
                .answer(musicDto.getAnswer())
                .hint(musicDto.getHint())
                .startTime(musicDto.getStartTime())
                .endTime(musicDto.getEndTime())
                .category(musicDto.getCategory())
                .mission(mission)
                .build();
        return musicRepository.save(music);

    }

    public boolean checkAnswer(Long roomId, String userAnswer) {
        return tracker.checkAnswer(roomId, userAnswer);
    }
    //음악 삭제
    public void delete(Music music) {
        musicRepository.delete(music);
    }


    //게임 시작시 음악 리스트 가져오기
    public MusicDto startMission(Long roomId, Long missionId) {
        List<Music> musicList = getMusicByMission(missionId);
        tracker.shuffleMusic(musicList);
        tracker.setMusicList(roomId, musicList);
        tracker.setCurrentMusic(roomId, 0);
        Music currentMusic = musicList.get(0);
        String rawAnswer = currentMusic.getAnswer();
        String categoryData = currentMusic.getCategory();
        tracker.createAnswer(roomId, rawAnswer, categoryData);
        if (!musicList.isEmpty()) {
            return transferMusicData(musicList, 0);
        }
        return null;
    }

    public AnswerDto getCurrentAnswer(Long roomId) {
        return tracker.getAnswerDto(roomId);
    }
    // 다음 음악으로 넘어가기
    public MusicDto getNextMusic(Long roomId) {
        tracker.nextMusic(roomId);
        List<Music> missionList = tracker.getMusicList(roomId);
        int currentIndex = tracker.getCurrentIndex(roomId);
        Music currentMusic = missionList.get(currentIndex);
        String rawAnswer = currentMusic.getAnswer();
        String categoryData = currentMusic.getCategory();
        tracker.createAnswer(roomId, rawAnswer, categoryData);
        if (currentIndex >= 0 && currentIndex < missionList.size()) {
            return transferMusicData(missionList, currentIndex);
        }
        return null;
    }

    public MusicDto getMusic(Long roomId) {
        List<Music> missionList = tracker.getMusicList(roomId);
        int currentIndex = tracker.getCurrentIndex(roomId);
        if (currentIndex >= 0 && currentIndex < missionList.size()) {
            return transferMusicData(missionList, currentIndex);
        }
        return null;
    }

    //음악 수정
    public Music updateMusic(Long id, MusicDto musicDto) {
        Music music = getMusicById(id);
        music.setTitle(musicDto.getTitle());
        music.setSong(musicDto.getSong());
        music.setYoutube_url(musicDto.getYoutube_url());
        music.setAnswer(musicDto.getAnswer());
        music.setHint(musicDto.getHint());
        music.setStartTime(musicDto.getStartTime());
        music.setEndTime(musicDto.getEndTime());
        music.setCategory(musicDto.getCategory());
        return musicRepository.save(music);
    }
}
