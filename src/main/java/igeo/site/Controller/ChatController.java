package igeo.site.Controller;

import igeo.site.DTO.ChatDto;
import igeo.site.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/message")
    public void message(@Payload ChatDto chatDto) {
        String roomId = chatDto.getRoomId();
        Object result = chatService.message(chatDto, Long.parseLong(roomId));
        template.convertAndSend("/chat/" + roomId, result);
    }

}
