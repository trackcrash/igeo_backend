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
    public ResponseEntity<?> saveMission(@RequestBody MissionDto missionDto) {
        return missionService.saveMission(missionDto);
    }

    //Update
    @PutMapping("/update")
    public ResponseEntity<?> updateMission(@RequestBody MissionDto missionDto) {
        return missionService.updateMission(missionDto);
    }

    //Delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMission(@PathVariable Long id) {
        return missionService.deleteMission(id);
    }

    //Read
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getMission(@PathVariable Long id) {
        return ResponseEntity.ok(missionService.getMission(id));
    }

    //Get All
    @GetMapping("/all")
    public ResponseEntity<?> getAllMissions() {
        return ResponseEntity.ok(missionService.getAllMission());
    }

    //OWNED MAPS
    @GetMapping("/owned")
    public ResponseEntity<?> getOwnedMaps() {
        return ResponseEntity.ok().body(missionService.getOwnedMaps());
    }
}
