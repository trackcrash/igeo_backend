package igeo.site.Controller;

import igeo.site.DTO.CreateRoomDto;
import igeo.site.DTO.RoomDto;
import igeo.site.Service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/room_list/create")
    public ResponseEntity<?> createRoom(@Valid @RequestBody CreateRoomDto createRoomDto) {
        return ResponseEntity.ok(roomService.createRoom(createRoomDto));
    }

    @DeleteMapping("/play/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable String roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/leave/{roomId}/{userName}")
    public ResponseEntity<?> leaveRoom(@Valid @PathVariable String roomId, @PathVariable String userName) {
        roomService.leaveRoom(roomId, userName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/room_list")
    public ResponseEntity<?> getRoom() {
        return ResponseEntity.ok(roomService.getRooms());
    }

    @PostMapping("/room_list/join")
    public ResponseEntity<?> joinRoom(@Valid @RequestBody RoomDto roomDto) {
        if (roomService.joinRoom(roomDto)) {
            return ResponseEntity.ok().build();
        }else {
            throw new IllegalArgumentException("오류가 발생했습니다");
        }

    }


}
