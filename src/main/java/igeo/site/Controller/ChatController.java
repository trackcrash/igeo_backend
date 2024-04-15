package igeo.site.Controller;

import igeo.site.DTO.*;
import igeo.site.Service.ChatService;
import igeo.site.Service.MissionService;
import igeo.site.Service.MusicService;
import igeo.site.Service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final RoomService roomService;
    private final MusicService musicService;
    private final MissionService missionService;
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
            template.convertAndSend("/startMission/" + skipDto.getRoomId(), musicDto);
        }
    }
    //새로고침 유지용
    @MessageMapping("/get_current_music")
    public void getCurrentMusic(@Payload SkipDto skipDto) {
        MusicDto musicDto = musicService.getMusic(Long.valueOf(skipDto.getRoomId()));
        template.convertAndSend("/startMission/" + skipDto.getRoomId(), musicDto);
    }

    @MessageMapping("/owner_skip")
    public void ownerSkip(@Payload SkipDto skipDto) {
        boolean result = roomService.ownerSkipVote(skipDto.getRoomId(), skipDto.getUserName());
        if(result){
            MusicDto musicDto = musicService.getNextMusic(Long.valueOf(skipDto.getRoomId()));
            template.convertAndSend("/skip/" + skipDto.getRoomId(), musicDto);
        }
    }

    @MessageMapping("/startMission")
    public void startMission(@Payload StartMissionDto startMissionDto) {
        MusicDto musicDto = musicService.startMission(startMissionDto.getRoomId(), startMissionDto.getMissionId());
        template.convertAndSend("/startMission/" + startMissionDto.getRoomId(), musicDto);
    }

    @MessageMapping("/missionSelect")
    public void missionSelect(@Payload StartMissionDto startMissionDto) {
        MissionDto missionDto = missionService.getMission(startMissionDto.getMissionId());
        roomService.selectMission(startMissionDto.getRoomId().toString(), startMissionDto.getMissionId());
        template.convertAndSend("/missionSelect/" + startMissionDto.getRoomId(), missionDto);
    }

    @MessageMapping("/get/{roomId}")
    public void getRoom(@PathVariable String roomId) {
        template.convertAndSend("/chat/" + roomId,roomService.getRoomStatus(roomId));
    }
}
