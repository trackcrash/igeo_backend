package igeo.site.Controller;

import igeo.site.DTO.MissionDto;
import igeo.site.Model.User;
import igeo.site.Service.MissionService;
import igeo.site.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/mission")
public class MissionController {

    private final MissionService missionService;
    private final UserService userService;

    //Create
    @PostMapping("/save")
    public ResponseEntity<MissionDto> saveMission(@RequestBody MissionDto missionDto) {
        missionService.saveMission(missionDto);
        return ResponseEntity.ok(missionDto);
    }

    //Update
    @PutMapping("/update/{id}")
    public ResponseEntity<MissionDto> updateMission(@RequestBody MissionDto missionDto, @PathVariable Long id) {
        missionService.updateMission(id, missionDto);
        return ResponseEntity.ok(missionDto);
    }

    //Delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MissionDto> deleteMission(@PathVariable Long id) {
        missionService.deleteMission(id);
        return ResponseEntity.ok().build();
    }

    //Read
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getMission(@PathVariable Long id) {
        return ResponseEntity.ok(missionService.getMission(id));
    }

    //OWNED MAPS
    @GetMapping("/owned")
    public ResponseEntity<?> getOwnedMaps() {
        User user = userService.getAuthenticatedUserInfo();
        return ResponseEntity.ok().body(missionService.getOwnedMaps(user.getId()));
    }
}
