package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class AnswerDto implements Serializable {
    private String category;
    private String answer;
    private String message;
    @Builder
    public AnswerDto(String category, String answer, String message) {
        this.category = category;
        this.answer = answer;
        this.message = message;
    }
}
