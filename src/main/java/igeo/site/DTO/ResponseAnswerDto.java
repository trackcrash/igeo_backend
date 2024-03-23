package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseAnswerDto implements Serializable {
    private AnswerDto answer;
    private ChatDto chat;

    @Builder
    public ResponseAnswerDto(AnswerDto answer, ChatDto chat) {
        this.answer = answer;
        this.chat = chat;
    }
}
