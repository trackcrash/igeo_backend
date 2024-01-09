package igeo.site.Controller;

import igeo.site.DTO.ChatDto;
import igeo.site.DTO.CreateRoomDto;
import igeo.site.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat/message")
    @SendTo("/chat/{roomId}")
    public ChatDto message(@Payload ChatDto chatDto, @DestinationVariable String roomId) {
        return chatService.message(chatDto, Long.parseLong(roomId));
    }

}
