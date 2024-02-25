package igeo.site.Service;

import igeo.site.DTO.ImageDto;
import igeo.site.DTO.MusicDto;
import igeo.site.Game.MissionTracker;
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
    public void delete(Image image)
    {
        imageRepository.delete(image);
    }
    public Image save(ImageDto imageDto, Mission mission) {
        Image image = Image.builder()
                .title(imageDto.getTitle())
                .imageUrl(imageDto.getImage_url())
                .answer(imageDto.getAnswer())
                .hint(imageDto.getHint())
                .startTime(imageDto.getStartTime())
                .endTime(imageDto.getEndTime())
                .category(imageDto.getCategory())
                .mission(mission)
                .build();
        return imageRepository.save(image);
    }
    public Image getImagesById(Long id) {
        return imageRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 이미지입니다.")
        );
    }
    public List<Image> getImageByMission(Long id) {
        return imageRepository.findByMission_Id(id);
    }
    public ImageDto transferImageData(List<Image> imageList, int index) {
        Image image = imageList.get(index);
        return ImageDto.builder()
                .id(image.getId())
                .title(image.getTitle())
                .image_url(image.getImageUrl())
                .answer(image.getAnswer())
                .hint(image.getHint())
                .startTime(image.getStartTime())
                .endTime(image.getEndTime())
                .category(image.getCategory())
                .build();
    }

    public Image updateImages(Long id, ImageDto imageDto) {
        Image image = getImagesById(id);
        image.setTitle(imageDto.getTitle());
        image.setImageUrl(imageDto.getImage_url());
        image.setAnswer(imageDto.getAnswer());
        image.setHint(imageDto.getHint());
        image.setStartTime(imageDto.getStartTime());
        image.setEndTime(imageDto.getEndTime());
        image.setCategory(imageDto.getCategory());
        return imageRepository.save(image);
    }
    public ImageDto startMission(Long roomId, Long missionId) {
        List<Image> imageList = getImageByMission(missionId);
        tracker.shuffleImage(imageList);
        tracker.setImageList(roomId, imageList);
        tracker.setCurrentImage(roomId, 0);
        Image currecntImage = imageList.get(0);
        String rawAnswer = currecntImage.getAnswer();
        String categoryData = currecntImage.getCategory();
        tracker.createAnswer(roomId, rawAnswer, categoryData);
        if (!imageList.isEmpty()) {
            return transferImageData(imageList, 0);
        }
        return null;
    }
    public ImageDto getNextImage(Long roomId) {
        tracker.nextMusic(roomId);
        List<Image> missionList = tracker.getImageList(roomId);
        int currentIndex = tracker.getCurrentIndex(roomId);
        Image currentImage = missionList.get(currentIndex);
        String rawAnswer = currentImage.getAnswer();
        String categoryData = currentImage.getCategory();
        tracker.createAnswer(roomId, rawAnswer, categoryData);
        if (currentIndex >= 0 && currentIndex < missionList.size()) {
            return transferImageData(missionList, currentIndex);
        }
        return null;
    }
}
