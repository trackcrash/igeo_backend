package igeo.site.Game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MissionTracker {
    private Map<Long, Integer> currentMusic = new ConcurrentHashMap<>();

    public int getCurrentMusic(Long roomId) {
        return currentMusic.get(roomId);
    }
}
