package igeo.site.Game;

import igeo.site.DTO.AnswerDto;
import igeo.site.Model.Answer;
import igeo.site.Model.Category;
import igeo.site.Model.Image;
import igeo.site.Model.Music;
import igeo.site.Service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class MissionTracker {
    private final Map<Long, Integer> currentIndex = new ConcurrentHashMap<>();
    private final Map<Long, List<Music>> musicLists = new ConcurrentHashMap<>();
    private final Map<Long, Answer> answers = new ConcurrentHashMap<>();
    private final Map<Long, List<Image>> imageLists = new ConcurrentHashMap<>();


    private final Map<Long, AnswerDto> answerDtos = new ConcurrentHashMap<>();

    //정답 생성
    public void createAnswer(Long roomId, String rawAnswer, String categoryData) {
        Map<String, Integer> categoryMap = parseCategories(categoryData);
        List<Object> answerList = parseAnswers(rawAnswer);
        Answer answer = processAnswer(answerList, categoryMap);
        answers.put(roomId, answer);
    }

    //정답 체크
    public boolean checkAnswer(Long roomId, String message) {
        Answer answer = getAnswer(roomId);
        if (answer == null) {
            return false;
        }

        for (Map.Entry<String, Category> entry : answer.getCategories().entrySet()) {
            Category category = entry.getValue();
            //System.out.println("카테고리: " + entry.getKey() + ", 정답: " + category.getPossibleAnswers());
            // 정답이 문자열인 경우
            for (Object possibleAnswer : new ArrayList<>(category.getPossibleAnswers())) {
                if (possibleAnswer instanceof String) {
                    if (message.equals(possibleAnswer)) {
                        addAnswerDto(roomId, message, entry.getKey());
                        category.clearPossibleAnswers();
                        return true;
                    }
                } else if (possibleAnswer instanceof List) { // 정답이 2차원 리스트인 경우
                    List<String> answerGroup = (List<String>) possibleAnswer;
                    if (answerGroup.contains(message)) {
                        addAnswerDto(roomId, message, entry.getKey());
                        category.removeAnswerGroup(answerGroup);
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public void addAnswerDto(Long roomId, String message, String categoryName) {
        if(answerDtos.containsKey(roomId)) {
            //기존 정답 삭제
            answerDtos.remove(roomId);
            //새로운 정답 추가
            AnswerDto answerDto = new AnswerDto(categoryName, message, null);
            answerDtos.put(roomId, answerDto);
        } else {
            AnswerDto answerDto = new AnswerDto(categoryName, message, null);
            answerDtos.put(roomId, answerDto);
        }
    }

    public AnswerDto getAnswerDto(Long roomId) {
        return answerDtos.get(roomId);
    }



    //정답 가져오기
    public Answer getAnswer(Long roomId) {
        return answers.get(roomId);
    }

    //정답 파싱
    public Map<String, Integer> parseCategories(String categoryStr) {
        Map<String, Integer> categoryMap = new HashMap<>();
        try {
            String[] categoryPairs = categoryStr.substring(1, categoryStr.length() - 1).split("\\],\\[");
            for (String pair : categoryPairs) {
                String[] parts = pair.split(":");
                String key = parts[0].trim();
                int value = parts.length > 1 && !parts[1].trim().isEmpty() ? Integer.parseInt(parts[1].trim()) : 0;
                categoryMap.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 출력
        }
        return categoryMap;
    }

    //정답 파싱
    public List<Object> parseAnswers(String answerStr) {
        List<Object> answerList = new ArrayList<>();
        String[] mainItems = answerStr.split("/");

        for (String mainItem : mainItems) {
            // ],[ 패턴을 확인해서 2차원 리스트인지 확인
            if (mainItem.contains("],[")) {
                List<List<String>> groupList = new ArrayList<>();
                String[] groupedItems = mainItem.split("\\],\\[");

                for (String item : groupedItems) {
                    //각 항목에서 '['와 ']'를 제거합니다.
                    item = item.replace("[", "").replace("]", "");
                    List<String> group = Arrays.asList(item.split(",\\s*"));
                    groupList.add(group);
                }
                answerList.add(groupList);
            } else {
                // 단일 정답 리스트 처리
                mainItem = mainItem.replace("[", "").replace("]", "");
                List<String> singleList = Arrays.asList(mainItem.split(",\\s*"));
                answerList.add(singleList);
            }
        }
        return answerList;
    }

    //정답 생성
    public Answer processAnswer(List<Object> answerList, Map<String, Integer> categoryMap) {

        Answer answer = new Answer();

        int index = 0;
        for (Map.Entry<String, Integer> entry : categoryMap.entrySet()) {
            String categoryName = entry.getKey();
            int requiredAnswers = entry.getValue();

            Object answersObj = answerList.get(index);

            Category category = new Category(requiredAnswers);
            if (answersObj instanceof List) {
                List<?> answers = (List<?>) answersObj;

                if (!answers.isEmpty() && answers.get(0) instanceof String) {
                    // 1차원 리스트 처리
                    for (Object ans : answers) {
                        category.addAnswer((String) ans);
                    }
                    index++;
                } else if (!answers.isEmpty() && answers.get(0) instanceof List) {
                    // 2차원 리스트 처리
                    List<List<String>> groupedAnswers = new ArrayList<>();
                    for (Object groupObj : answers) {
                        List<?> group = (List<?>) groupObj;
                        List<String> groupAnswers = new ArrayList<>();
                        for (Object ans : group) {
                            groupAnswers.add((String) ans);
                        }
                        groupedAnswers.add(groupAnswers);
                    }
                    category.addGroupedAnswers(groupedAnswers); // 2차원 리스트 추가
                    index++;
                }
            }
            answer.addCategory(categoryName, category);
        }

        return answer;
    }

    //현재 음악 인덱스 가져오기
    public int getCurrentIndex(Long roomId) {
        return currentIndex.getOrDefault(roomId, -1);
    }
    //현재 음악 인덱스 저장
    public void setCurrentMusic(Long roomId, int index) {
        currentIndex.put(roomId, index);
    }

    //현재 음악 인덱스 저장
    public void setCurrentImage(Long roomId, int index) {
        currentIndex.put(roomId, index);
    }
    //음악 리스트 섞기
    public void shuffleMusic(List<Music> musicList){
        Collections.shuffle(musicList);
    }
    public void shuffleImage(List<Image> imageList){
        Collections.shuffle(imageList);
    }
    //다음 곡 가져오기
    public void nextMusic(Long roomId) {
        int newCurrentIndex = getCurrentIndex(roomId);
        if (newCurrentIndex >= 0 && newCurrentIndex < musicLists.get(roomId).size() - 1) {
            currentIndex.put(roomId, newCurrentIndex + 1);
        }

    }
    //게임 종료
    public void endGame(Long roomId) {
        currentIndex.remove(roomId);
        musicLists.remove(roomId);
        answers.remove(roomId);
        answerDtos.remove(roomId);
    }
    public void nextImage(Long roomId) {
        int newCurrentIndex = getCurrentIndex(roomId);
        if (newCurrentIndex >= 0 && newCurrentIndex < imageLists.get(roomId).size() - 1) {
            currentIndex.put(roomId, newCurrentIndex + 1);
        }
    }

    //음악 리스트 저장(방번호, 음악리스트)
    public void setMusicList(Long roomId, List<Music> musicList) {
        musicLists.put(roomId, musicList);
    }
    public void setImageList(Long roomId, List<Image> imageList) {
        imageLists.put(roomId, imageList);
    }
    //음악 리스트 가져오기
    public List<Music> getMusicList(Long roomId) {
        return musicLists.getOrDefault(roomId, new ArrayList<>());
    }
    public List<Image> getImageList(Long roomId) {
        return imageLists.getOrDefault(roomId, new ArrayList<>());
    }
}
