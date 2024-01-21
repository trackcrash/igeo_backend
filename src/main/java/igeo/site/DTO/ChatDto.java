package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatDto {
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;

    public enum MessageType {
        ENTER, TALK, QUIT;
    }

    @Builder
    public ChatDto(MessageType type, String roomId, String sender, String message) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
    }
}
