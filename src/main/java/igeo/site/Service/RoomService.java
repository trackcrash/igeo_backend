package igeo.site.Service;

import igeo.site.DTO.CreateRoomDto;
import igeo.site.DTO.RoomDto;
import igeo.site.Game.RoomTracker;
import igeo.site.Model.Room;
import igeo.site.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    private UserService userService;

    private final RoomTracker roomTracker = new RoomTracker();
    //방생성
    public Room createRoom(CreateRoomDto createRoomDto) {
        User user = userService.getUserByName(createRoomDto.getSender());
        return roomTracker.createRoom(createRoomDto, user);
    }
    //방에 유저추가
    public boolean joinRoom(RoomDto roomDto){
        User user = userService.getUserById(roomDto.getUserId());
        String roomId = roomDto.getRoomId();
        if(user != null && roomId != null && roomTracker.getRoom(roomId).getPassword() == roomDto.getPassword() && roomTracker.getRoom(roomId).getCurrentUsers() < roomTracker.getRoom(roomId).getMaxUsers()) {
            return roomTracker.addUser(roomId, user);
        } else {
            return false;
        }
    }

    //방제거
    public void deleteRoom(String roomId) {
        roomTracker.deleteRoom(roomId);
    }
    //방리스트
    public List<Room> getRooms() {
        return roomTracker.getRooms();
    }
    //퇴장
    public void leaveRoom(String roomId, String userName) {
        User user = userService.getUserByName(userName);
        roomTracker.leaveRoom(roomId, user);
        if (roomTracker.getRoom(roomId).getCurrentUsers() == 0) {
            deleteRoom(roomId);
        }
    }

}
