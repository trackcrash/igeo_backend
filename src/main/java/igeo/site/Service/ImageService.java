package igeo.site.Service;

import igeo.site.DTO.AnswerDto;
import igeo.site.DTO.ImageDto;
import igeo.site.DTO.MusicDto;
import igeo.site.Game.MissionTracker;
import igeo.site.Game.RoomTracker;
import igeo.site.Model.Image;
import igeo.site.Model.Mission;
import igeo.site.Model.Music;
import igeo.site.Repository.ImageRepository;
import igeo.site.Repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ImageService {
    private final ImageRepository imageRepository;
    private final MissionTracker tracker;
    private final RoomTracker roomTracker;

    //미션 시작시 이미지 리스트 가져오기
    //결과값 : 이미지 리스트
    public List<Image> getImageByMission(Long id) {
        return imageRepository.findByMission_Id(id);
    }
    //List에서 하나씩 꺼내서 ImageDto로 변환
    public ImageDto transferImageData(List<Image> imageList, int index) {
        Image image = imageList.get(index);
        return ImageDto.builder()
                .id(image.getId())
                .title(image.getTitle())
                .imageUrl(image.getImageUrl())
                .answer(image.getAnswer())
                .hint(image.getHint())
                .startTime(image.getStartTime())
                .endTime(image.getEndTime())
                .category(image.getCategory())
                .CurrentImageIndex(index)
                .TotalImageCount(imageList.size())
                .build();
    }

    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 이미지입니다.")
        );
    }

    //이미지 저장
    public Image save(ImageDto imageDto, Mission mission) {
        Image image = Image.builder()
                .title(imageDto.getTitle())
                .imageUrl(imageDto.getImageUrl())
                .answer(imageDto.getAnswer())
                .hint(imageDto.getHint())
                .mission(mission)
                .startTime(imageDto.getStartTime())
                .endTime(imageDto.getEndTime())
                .category(imageDto.getCategory())
                .build();
        return imageRepository.save(image);
    }

    //이미지 삭제
    public void delete(Image image) {
        imageRepository.delete(image);
    }

    //이미지 업데이트
    public void updateImages(Long missionId, ImageDto imageDto){
        Image image = getImageById(missionId);
        image.setTitle(imageDto.getTitle());
        image.setImageUrl(imageDto.getImageUrl());
        image.setAnswer(imageDto.getAnswer());
        image.setHint(imageDto.getHint());
        image.setStartTime(imageDto.getStartTime());
        image.setEndTime(imageDto.getEndTime());
        image.setCategory(imageDto.getCategory());
    }

    //게임 시작시 이미지 리스트 가져오기
    public ImageDto startMission(Long roomId, Long missionId) {
        List<Image> imageList = getImageByMission(missionId);
        tracker.shuffleImage(imageList);
        tracker.setImageList(roomId, imageList);
        tracker.setCurrentImage(roomId, 0);
        roomTracker.startGame(roomId.toString());
        Image currentImage = imageList.get(0);
        String rawAnswer = currentImage.getAnswer();
        String categoryData = currentImage.getCategory();
        tracker.createAnswer(roomId, rawAnswer, categoryData);
        if (!imageList.isEmpty()) {
            return transferImageData(imageList, 0);
        }
        return null;
    }

    //정답 가져오기
    public AnswerDto getCurrentAnswer(Long roomId) {
        return tracker.getAnswerDto(roomId);
    }

    // 다음 이미지으로 넘어가기
    public ImageDto getNextImage(Long roomId) {
        tracker.nextImage(roomId);
        List<Image> imageList = tracker.getImageList(roomId);
        int currentIndex = tracker.getCurrentIndex(roomId);

        if (currentIndex < imageList.size()) {
            Image currentImage = imageList.get(currentIndex);
            String rawAnswer = currentImage.getAnswer();
            String categoryData = currentImage.getCategory();
            tracker.createAnswer(roomId, rawAnswer, categoryData);
            return transferImageData(imageList, currentIndex);
        }
        tracker.endGame(roomId);
        return null;
    }


    //이미지 가져오기
    public ImageDto getImage(Long roomId) {
        List<Image> missionList = tracker.getImageList(roomId);
        int currentIndex = tracker.getCurrentIndex(roomId);
        if (currentIndex >= 0 && currentIndex < missionList.size()) {
            return transferImageData(missionList, currentIndex);
        }
        return null;
    }

}
