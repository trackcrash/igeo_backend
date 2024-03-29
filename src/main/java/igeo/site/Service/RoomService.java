package igeo.site.Service;

import igeo.site.DTO.*;
import igeo.site.Game.RoomTracker;
import igeo.site.Model.Mission;
import igeo.site.Model.Room;
import igeo.site.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final UserService userService;
    private final RoomTracker roomTracker;
    private final MissionService missionService;


    //방생성
    public Room createRoom(CreateRoomDto createRoomDto) {
        User user = userService.getAuthenticatedUserInfo();
        return roomTracker.createRoom(createRoomDto, user);
    }
    //방에 유저추가
    public boolean joinRoom(RoomDto roomDto) {
        User user = userService.getAuthenticatedUserInfo();
        String roomId = roomDto.getRoomId();
        String password = roomDto.getPassword();
        if(user != null && roomId != null && roomTracker.getRoom(roomId).getPassword().equals(password) && roomTracker.getRoom(roomId).getCurrentUsersCount() < roomTracker.getRoom(roomId).getMaxUsers()) {
            return roomTracker.addUser(roomId, user);
        } else {
            return false;
        }
    }
    //스킵투표
    public boolean addSkipVote(String roomId, String userName) {
        User user = userService.getUserByName(userName);
        return roomTracker.addSkipVote(roomId, user);
    }

    //방장스킵
    public boolean ownerSkipVote(String roomId, String userName) {
        User user = userService.getUserByName(userName);
        return roomTracker.ownerSkipVote(roomId, user);
    }

    //방제거
    public void deleteRoom(String roomId) {
        roomTracker.deleteRoom(roomId);
    }
    //방리스트
    public List<RoomListDto> getRooms() {
        List<Room> rooms = roomTracker.getRooms();
        List<RoomListDto> roomListDtos = new ArrayList<>();
        for(Room room : rooms){
            RoomListDto temp = new RoomListDto();
            temp.setRoomId(room.getRoomId());
            temp.setType(room.getType());
            temp.setRoomName(room.getRoomName());
            temp.setOwner(room.getOwner());
            temp.setMaxUsers(room.getMaxUsers());
            temp.setCurrentUsers(room.getCurrentUsersCount());
            if(room.getMissionId()!=null){
                Mission mission = missionService.getMissionById(room.getMissionId());
                temp.setMapType(mission.getMapType());
                temp.setThumbnail(mission.getThumbnail());
            }else{
                temp.setMapType(null);
                temp.setThumbnail(null);
            }
            roomListDtos.add(temp);
        }
        return roomListDtos;
    }
    //퇴장
    public void leaveRoom(String roomId, String userName) {
        User user = userService.getUserByName(userName);
        roomTracker.leaveRoom(roomId, user);
        if (roomTracker.getRoom(roomId).getCurrentUsersCount() == 0) {
            roomTracker.deleteRoom(roomId);
        }
    }

    //미션 선택
    public void selectMission(String roomId, Long missionId) {
        roomTracker.selectMission(roomId, missionId);
    }
    private String nickname;
    private int character;
    private int level;
    public ResponseEntity<?> getRoomStatus(String roomId) {
        Room room = roomTracker.getRoom(roomId);
        List<RoomUserInfo> roomUserInfos = new ArrayList<>();

        for(Long userId : room.getCurrentUsers()){
            User user = userService.getUserById(userId);
            RoomUserInfo roomUserInfo = RoomUserInfo.builder()
                    .nickname(user.getName())
                    .character(user.getCharacter())
                    .level(user.getLevel())
                    .build();
            roomUserInfos.add(roomUserInfo);
        }

        return ResponseEntity.ok(RoomStatusDto.builder()
                        .type(room.getType())
                        .roomName(room.getRoomName())
                        .owner(room.getOwner())
                        .maxUsers(room.getMaxUsers())
                        .currentUsers(roomUserInfos)
                        .missionId(room.getMissionId())
                        .currentUsers(roomUserInfos)
                        .roomId(roomId)
                .build());
    }
}
