package igeo.site.Model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class Category {
    private List<Object> possibleAnswers;
    private int requiredAnswers;

    public Category(int requiredAnswers) {
        this.requiredAnswers = requiredAnswers;
        this.possibleAnswers = new ArrayList<>();
    }

    // 단일 답변을 저장합니다.
    public void addAnswer(Object answer) {
        this.possibleAnswers.add(answer);
    }

    // 2차원 리스트를 저장합니다.
    public void addGroupedAnswers(List<List<String>> groupedAnswers) {
        for (List<String> group : groupedAnswers) {
            possibleAnswers.add(group); // 2차원 리스트를 저장
        }
    }

    // 2차원 리스트에서 단일 답변을 제거합니다.
    public void removeAnswerGroup(List<String> answerGroup) {
        possibleAnswers.removeIf(answer -> answer.equals(answerGroup));
    }

    //답변 리스트에서 답변 제거
    public void clearPossibleAnswers() {
        possibleAnswers.clear();
    }

    //카테고리 정답이 전부 끝났는지 확인
    public boolean isAnswerComplete() {
        int count = 0;
        for (Object ans : possibleAnswers) {
            if (ans instanceof String) {
                count++;
            } else if (ans instanceof List) {
                count += ((List<?>) ans).size();
            }
        }
        return count >= requiredAnswers;
    }
}
