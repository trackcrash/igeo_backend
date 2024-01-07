package igeo.site.Controller;

import igeo.site.DTO.ChatDto;
import igeo.site.DTO.CreateRoomDto;
import igeo.site.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/play")
public class ChatController {
    @Autowired
    private ChatService chatService;

}
