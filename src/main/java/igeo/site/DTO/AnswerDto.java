package igeo.site.DTO;

import lombok.Builder;
import lombok.Data;

@Data
public class AnswerDto {
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
