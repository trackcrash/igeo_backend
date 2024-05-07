package igeo.site.Controller;

import igeo.site.DTO.*;
import igeo.site.Model.Mission;
import igeo.site.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final RoomService roomService;
    private final ImageService imageService;
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
        if (result) {
            MusicDto musicDto = musicService.getNextMusic(Long.valueOf(skipDto.getRoomId()));
            if (musicDto == null) {
                // 게임 종료 로직
                List<EndOfGameDto> endOfGameDtoList = roomService.endGame(skipDto.getRoomId());
                template.convertAndSend("/startMission/" + skipDto.getRoomId(), endOfGameDtoList);
            } else {
                // 다음 음악 재생 로직
                template.convertAndSend("/startMission/" + skipDto.getRoomId(), musicDto);
            }
        } else {
            // 스킵 투표 실패 시 현재 투표 수 전송
            int count = roomService.getRoomCount(skipDto.getRoomId());
            template.convertAndSend("/skip/" + skipDto.getRoomId(), count);
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
        String flag = missionService.getMissionById(startMissionDto.getMissionId()).getMapType();
        if (flag.equals("MUSIC")) {
            MusicDto musicDto = musicService.startMission(startMissionDto.getRoomId(), startMissionDto.getMissionId());
            template.convertAndSend("/startMission/" + startMissionDto.getRoomId(), musicDto);
        } else {
            ImageDto imageDto = imageService.startMission(startMissionDto.getRoomId(), startMissionDto.getMissionId());
            template.convertAndSend("/startMission/" + startMissionDto.getRoomId(), imageDto);
        }
    }

    @MessageMapping("/missionSelect")
    public void missionSelect(@Payload StartMissionDto startMissionDto) {
        MissionDto missionDto = missionService.getMission(startMissionDto.getMissionId());
        roomService.selectMission(startMissionDto.getRoomId().toString(), startMissionDto.getMissionId());
        template.convertAndSend("/missionSelect/" + startMissionDto.getRoomId(), missionDto);
    }

    @MessageMapping("/get/{roomId}")
    public void getRoom(@DestinationVariable String roomId) {
        template.convertAndSend("/chat/" + roomId,roomService.getRoomStatus(roomId));
    }
}
