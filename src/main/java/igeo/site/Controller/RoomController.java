package igeo.site.Controller;

import igeo.site.DTO.CreateRoomDto;
import igeo.site.DTO.RoomDto;
import igeo.site.DTO.SkipDto;
import igeo.site.Model.User;
import igeo.site.Service.RoomService;
import igeo.site.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("api/room")
public class RoomController {

    private final RoomService roomService;

    //방 생성
    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@Valid @RequestBody CreateRoomDto createRoomDto) {
        return ResponseEntity.ok(roomService.createRoom(createRoomDto));
    }

    //방 삭제
    @DeleteMapping("/play/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable String roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok().build();
    }

    //방 나가기(유저)
    @DeleteMapping("/leave/{roomId}/{userName}")
    public ResponseEntity<?> leaveRoom(@Valid @PathVariable String roomId, @PathVariable String userName) {
        roomService.leaveRoom(roomId, userName);
        return ResponseEntity.ok().build();
    }

    //방 리스트 가져오기
    @GetMapping("/room_list")
    public ResponseEntity<?> getRoom() {
        return ResponseEntity.ok(roomService.getRooms());
    }

    //방 참가
    @PostMapping("/join")
    public ResponseEntity<?> joinRoom(@Valid @RequestBody RoomDto roomDto) {
        if (roomService.joinRoom(roomDto)) {
            return ResponseEntity.ok().build();
        }else {
            throw new IllegalArgumentException("오류가 발생했습니다");
        }

    }

    @PostMapping("/skip")
    public ResponseEntity<?> addSkipVote(@Valid @RequestBody SkipDto skipDto) {
        boolean result = roomService.addSkipVote(skipDto.getRoomId(), skipDto.getUserName());
        if (result) {
            return ResponseEntity.ok().body(true);
        }else{
            return ResponseEntity.ok().body(false);
        }
    }

    @PostMapping("/owner_skip")
    public ResponseEntity<?> ownerSkipVote(@Valid @RequestBody SkipDto skipDto) {
        if (roomService.ownerSkipVote(skipDto.getRoomId(), skipDto.getUserName())) {
            return ResponseEntity.ok().build();
        }else {
            throw new IllegalArgumentException("방장이 아닙니다");
        }
    }

    @PostMapping("/play/{roomId}/{missionId}")
    public ResponseEntity<?> setMission(@PathVariable String roomId, @PathVariable Long missionId) {
        roomService.selectMission(roomId, missionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable String roomId) {
        return ResponseEntity.ok(roomService.getRoomStatus(roomId));
    }

}
