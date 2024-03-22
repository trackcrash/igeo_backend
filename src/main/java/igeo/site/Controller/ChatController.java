package igeo.site.Controller;

import igeo.site.DTO.ChatDto;
import igeo.site.DTO.MusicDto;
import igeo.site.DTO.SkipDto;
import igeo.site.DTO.StartMissionDto;
import igeo.site.Service.ChatService;
import igeo.site.Service.MusicService;
import igeo.site.Service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final RoomService roomService;
    private final MusicService musicService;
    private final SimpMessagingTemplate template;

    @MessageMapping("/message")
    public void message(@Payload ChatDto chatDto) {
        String roomId = chatDto.getRoomId();
        Object result = chatService.message(chatDto, Long.parseLong(roomId));
        template.convertAndSend("/chat/" + roomId, result);
    }

    @MessageMapping("/skip")
    public void skip(@Payload SkipDto skipDto) {
        boolean result = roomService.addSkipVote(skipDto.getRoomId(), skipDto.getUserName());
        if(result){
            MusicDto musicDto = musicService.getNextMusic(Long.valueOf(skipDto.getRoomId()));
            template.convertAndSend("/skip/" + skipDto.getRoomId(), musicDto);
        }
    }

    @MessageMapping("/owner_skip")
    public void ownerSkip(@Payload SkipDto skipDto) {
        boolean result = roomService.ownerSkipVote(skipDto.getRoomId(), skipDto.getUserName());
        if(result){
            MusicDto musicDto = musicService.getNextMusic(Long.valueOf(skipDto.getRoomId()));
            template.convertAndSend("/skip/" + skipDto.getRoomId(), musicDto);
        }
    }

    @MessageMapping("/StartMission")
    public void startMission(@Payload StartMissionDto startMissionDto) {
        MusicDto musicDto = musicService.startMission(startMissionDto.getRoomId(), startMissionDto.getMissionId());
        template.convertAndSend("/startMission/" + startMissionDto.getRoomId(), musicDto);
    }

}
