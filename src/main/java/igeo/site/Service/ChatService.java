package igeo.site.Service;

import igeo.site.DTO.ChatDto;
import igeo.site.DTO.MusicDto;
import igeo.site.Game.MissionTracker;
import igeo.site.Game.RoomTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final MissionTracker missionTracker;
    private final MusicService musicService;

    @Autowired
    public ChatService(MusicService musicService, MissionTracker missionTracker) {
        this.missionTracker = missionTracker;
        this.musicService = musicService;
    }

    public ChatDto message(ChatDto chatDto,Long roomId) {
        switch(chatDto.getType()) {
            case ENTER:
                chatDto.setMessage(chatDto.getSender() + "님이 입장하셨습니다.");
                break;
            case TALK:
                String msg = chatDto.getMessage();
                if(missionTracker.checkAnswer(roomId, msg)){
                    chatDto.setMessage(chatDto.getSender() + "님이 정답을 맞췄습니다"+ msg);
                }else{
                    chatDto.setMessage(chatDto.getSender() + " : " + msg);
                }
                break;
            case QUIT:
                chatDto.setMessage(chatDto.getSender() + "님이 퇴장하셨습니다.");
                break;
        }
        return chatDto;
    }

}
