package igeo.site.Service;

import igeo.site.DTO.AnswerDto;
import igeo.site.DTO.ChatDto;
import igeo.site.DTO.ResponseAnswerDto;
import igeo.site.Game.MissionTracker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MissionTracker missionTracker;
    private final MusicService musicService;

    public Object message(ChatDto chatDto, Long roomId) {
        switch(chatDto.getType()) {
            case ENTER:
                chatDto.setSender("System");
                chatDto.setMessage(chatDto.getSender() + "님이 입장하셨습니다.");
                break;
            case TALK:
                String msg = chatDto.getMessage();
                if(missionTracker.checkAnswer(roomId, msg)){
                    AnswerDto currentAnswer = musicService.getCurrentAnswer(roomId);
                    chatDto.setSender("System");
                    chatDto.setMessage(chatDto.getSender() + "님이 정답을 맞추셨습니다.");
                    return ResponseAnswerDto.builder()
                            .answer(currentAnswer)
                            .chat(chatDto)
                            .build();
                }else{
                    chatDto.setMessage(msg);
                }
                break;
            case QUIT:
                chatDto.setSender("System");
                chatDto.setMessage(chatDto.getSender() + "님이 퇴장하셨습니다.");
                break;
        }
        return chatDto;
    }

}
