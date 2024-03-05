package igeo.site.Service;

import igeo.site.DTO.ImageDto;
import igeo.site.DTO.MissionDto;
import igeo.site.DTO.MissionSelectDto;
import igeo.site.DTO.MusicDto;
import igeo.site.Model.Image;
import igeo.site.Model.Mission;

import igeo.site.Model.Music;
import igeo.site.Model.User;
import igeo.site.Repository.ImageRepository;
import igeo.site.Repository.MissionRepository;
import igeo.site.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final UserRepository userRepository;

    //음악 저장시 사용하기위해 서비스객체 주입
    private final MusicService musicService;
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    private final UserService userService;
    //미션 저장
    //클라이언트에서 받은 정보를 미션객체로 만들어서 저장후 음악저장
    //mission_id가 필요하기 때문에 미션 저장 후 음악 저장
    public MissionDto saveMission(MissionDto missionDto) {
        boolean flag = missionDto.getMapType().equals("MUSIC");
        int questionCount = 0;
        if(flag) {
            questionCount = missionDto.getMusics().size();
        }else{
            questionCount = missionDto.getImages().size();
        }
        //클라이언트에서 받은 정보를 미션객체화
        Mission mission = Mission.builder()
                .MapName(missionDto.getMapName())
                .MapProducer(missionDto.getMapProducer())
                .Thumbnail(missionDto.getThumbnail())
                .active(true)
                .PlayNum(0)
                .Description(missionDto.getDescription())
                .user(userRepository.findById(missionDto.getUser_id()).orElse(null))
                .numberOfQuestion(questionCount)
                .mapType(missionDto.getMapType())
                .build();
        missionRepository.save(mission);
        if(flag){
            //미션리스트 순회하며 음악 저장
            for (int i = 0; i < missionDto.getMusics().size(); i++) {
                musicService.save(missionDto.getMusics().get(i), mission);
            }
        }else{
            //미션리스트 순회하며 이미지 저장
            for (int i = 0; i < missionDto.getImages().size(); i++) {
                imageService.save(missionDto.getImages().get(i), mission);
            }
        }

        return missionDto;
    }

    //미션 수정
    public MissionDto updateMission(MissionDto missionDto) {
        Mission mission = missionRepository.findById(missionDto.getId()).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 미션입니다.")
        );
        boolean flag = missionDto.getMapType().equals("MUSIC");
        User user = userService.getAuthenticatedUserInfo();
        if(user == null) throw new EntityNotFoundException("잘못된 접근입니다.");

        // 권한 확인
        if (!mission.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("유저정보가 다릅니다");
        }

        mission.setMapName(missionDto.getMapName());
        mission.setMapProducer(missionDto.getMapProducer());
        mission.setThumbnail(missionDto.getThumbnail());
        mission.setDescription(missionDto.getDescription());
        mission.setUser(userRepository.findById(missionDto.getUser_id()).orElse(null));
        if(flag)
        {
            mission.setNumberOfQuestion(missionDto.getMusics().size());
        }
        else
        {
            mission.setNumberOfQuestion(missionDto.getImages().size());
        }
        missionRepository.save(mission);

        if(flag) {
            for (int i = 0; i < missionDto.getMusics().size(); i++) {
                musicService.updateMusic(missionDto.getMusics().get(i).getId(), missionDto.getMusics().get(i));
            }
        }
        else
        {
            for (int i = 0; i < missionDto.getImages().size(); i++) {
                imageService.updateImages(missionDto.getImages().get(i).getId(), missionDto.getImages().get(i));
            }
        }

        return missionDto;
    }

    //미션 삭제
    public void deleteMission(Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 미션입니다.")
        );
        List<Music> musics = musicService.getMusicByMission(missionId);
        if(!musics.isEmpty()) {
            for (Music music : musics) {
                musicService.delete(music);
            }
        }
        List<Image> images = imageService.getImageByMission(missionId);
        if(!images.isEmpty())
        {
            for(Image image : images)
            {
                imageService.delete(image);
            }
        }

        // 사용자 인증 정보 확인
        User user = userService.getAuthenticatedUserInfo();
        if(user == null) throw new EntityNotFoundException("잘못된 접근입니다");
        missionRepository.delete(mission);

    }

    //미션선택 목록 조회
    public List<MissionSelectDto> getAllMission(){
        List<Mission> missions = missionRepository.findAll();
        List<MissionSelectDto> missionSelectDtos = new ArrayList<>();
        for (Mission mission : missions) {
            missionSelectDtos.add(MissionSelectDto.builder()
                    .id(mission.getId())
                    .thumbnail(mission.getThumbnail())
                    .mapName(mission.getMapName())
                    .description(mission.getDescription())
                    .mapType(mission.getMapType())
                    .numberOfQuestion(mission.getNumberOfQuestion())
                    .build());
        }
        return missionSelectDtos;
    }

    //미션 조회
    public MissionDto getMission(Long missionId) {
        Mission mission = missionRepository.findById(missionId).
                orElseThrow(() -> new IllegalStateException("존재하지 않는 미션입니다."));
        List<Music> musics = new ArrayList<>();
        musics = musicService.getMusicByMission(missionId);
        List<MusicDto> musicDtos = new ArrayList<>();
        for(int i = 0; i < musics.size(); i++) {
            musicDtos.add(musicService.transferMusicData(musics, i));
        }
        List<Image> images = new ArrayList<>();
        images = imageService.getImageByMission(missionId);
        List<ImageDto> imageDtos = new ArrayList<>();
        for(int i = 0; i < images.size(); i++) {
            imageDtos.add(imageService.transferImageData(images, i));
        }
        return MissionDto.builder()
                .id(mission.getId())
                .MapName(mission.getMapName())
                .MapProducer(mission.getMapProducer())
                .Thumbnail(mission.getThumbnail())
                .active(mission.isActive())
                .PlayNum(mission.getPlayNum())
                .Description(mission.getDescription())
                .user_id(mission.getUser().getId())
                .musics(musicDtos)
                .images(imageDtos)
                .numberOfQuestion(mission.getNumberOfQuestion())
                .build();
    }

    public List<Mission> getOwnedMaps() {
        User user = userService.getAuthenticatedUserInfo();
        List<Mission> missions = missionRepository.findByUserId(user.getId());
        //user는 제외
        for (Mission mission : missions) {
            mission.setUser(null);
        }
        return missions;
    }

    //미션 아이디로 미션 조회
    public Mission getMissionById(Long missionId) {
        return missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 미션입니다."));
    }

}
