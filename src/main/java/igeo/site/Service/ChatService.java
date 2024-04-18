package igeo.site.Service;

import igeo.site.DTO.AnswerDto;
import igeo.site.DTO.ChatDto;
import igeo.site.DTO.ResponseAnswerDto;
import igeo.site.Game.LevelManager;
import igeo.site.Game.MissionTracker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MissionTracker missionTracker;
    private final MusicService musicService;
    private final LevelManager levelManager;

    public Object message(ChatDto chatDto, Long roomId) {
        switch(chatDto.getType()) {
            case ENTER:
                chatDto.setMessage(chatDto.getSender() + "님이 입장하셨습니다.");
                chatDto.setSender("System");
                break;
            case TALK:
                String msg = chatDto.getMessage();
                if(missionTracker.checkAnswer(roomId, msg)){
                    AnswerDto currentAnswer = musicService.getCurrentAnswer(roomId);
                    currentAnswer.setMessage(chatDto.getMessage());
                    levelManager.addExp(chatDto.getSender());
                    chatDto.setMessage(chatDto.getSender() + "님이 정답을 맞추셨습니다.");
                    chatDto.setSender("System");
                    return ResponseAnswerDto.builder()
                            .answer(currentAnswer)
                            .chat(chatDto)
                            .build();
                }else{
                    chatDto.setMessage(msg);
                }
                break;
            case QUIT:
                chatDto.setMessage(chatDto.getSender() + "님이 퇴장하셨습니다.");
                chatDto.setSender("System");
                break;
        }
        return chatDto;
    }

}
