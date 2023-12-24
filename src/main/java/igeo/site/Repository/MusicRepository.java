package igeo.site.Repository;

import igeo.site.Model.Mission;
import igeo.site.Model.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {
    List<Music> findByMission_Id(Long missionId);
}
