package igeo.site.Game;

import igeo.site.Model.Music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MissionTracker {
    private Map<Long, Integer> currentMusicIndex = new ConcurrentHashMap<>();
    private Map<Long, List<Music>> musicLists = new ConcurrentHashMap<>();
    //현재 음악 인덱스 가져오기
    public int getCurrentMusicIndex(Long roomId) {
        return currentMusicIndex.getOrDefault(roomId, -1);
    }
    //현재 음악 인덱스 저장
    public void setCurrentMusic(Long roomId, int index) {
        currentMusicIndex.put(roomId, index);
    }
    //음악 리스트 섞기
    public void shuffleMusic(List<Music> musicList){
        Collections.shuffle(musicList);
    }
    //다음 곡 가져오기
    public void nextMusic(Long roomId) {
        int currentIndex = getCurrentMusicIndex(roomId);
        if (currentIndex >= 0 && currentIndex < musicLists.get(roomId).size() - 1) {
            currentMusicIndex.put(roomId, currentIndex + 1);
        }
    }
    //음악 리스트 저장(방번호, 음악리스트)
    public void setMusicList(Long roomId, List<Music> musicList) {
        musicLists.put(roomId, musicList);
    }
    //음악 리스트 가져오기
    public List<Music> getMusicList(Long roomId) {
        return musicLists.getOrDefault(roomId, new ArrayList<>());
    }
}
