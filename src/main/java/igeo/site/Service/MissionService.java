package igeo.site.Service;

import igeo.site.DTO.MissionDto;
import igeo.site.Model.Mission;

import igeo.site.Repository.MissionRepository;
import igeo.site.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MissionService {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private UserRepository userRepository;

    //음악 저장시 사용하기위해 서비스객체 주입
    @Autowired
    private MusicService musicService;

    //미션 저장
    //클라이언트에서 받은 정보를 미션객체로 만들어서 저장후 음악저장
    //mission_id가 필요하기 때문에 미션 저장 후 음악 저장
    public MissionDto saveMission(MissionDto missionDto) {
        //클라이언트에서 받은 정보를 미션객체화
        Mission mission = Mission.builder()
                .MapName(missionDto.getMapName())
                .MapProducer(missionDto.getMapProducer())
                .Thumbnail(missionDto.getThumbnail())
                .active(true)
                .PlayNum(0)
                .Description(missionDto.getDescription())
                .user(userRepository.findById(missionDto.getUser_id()).orElse(null))
                .build();
        missionRepository.save(mission);

        //미션리스트 순회하며 음악 저장
        for (int i = 0; i < missionDto.getMusics().size(); i++) {
            musicService.save(missionDto.getMusics().get(i), mission);
        }

        return missionDto;
    }

    //미션 수정
    public MissionDto updateMission(Long missionId, MissionDto missionDto) {
        Mission mission = missionRepository.findById(missionId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 미션입니다.")
        );
        mission.setMapName(missionDto.getMapName());
        mission.setMapProducer(missionDto.getMapProducer());
        mission.setThumbnail(missionDto.getThumbnail());
        mission.setDescription(missionDto.getDescription());
        mission.setUser(userRepository.findById(missionDto.getUser_id()).orElse(null));
        missionRepository.save(mission);

        for(int i = 0; i < missionDto.getMusics().size(); i++) {
            musicService.updateMusic(missionDto.getMusics().get(i).getId(), missionDto.getMusics().get(i));
        }

        return missionDto;
    }

    //미션 삭제
    public void deleteMission(Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 미션입니다.")
        );
        missionRepository.delete(mission);
    }
}
