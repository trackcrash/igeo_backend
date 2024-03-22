package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;

@Data
public class ResponseAnswerDto {
    private AnswerDto answer;
    private ChatDto chat;

    @Builder
    public ResponseAnswerDto(AnswerDto answer, ChatDto chat) {
        this.answer = answer;
        this.chat = chat;
    }
}
