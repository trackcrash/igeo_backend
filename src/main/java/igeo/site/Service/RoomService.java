package igeo.site.Service;

import igeo.site.DTO.*;
import igeo.site.Game.RoomTracker;
import igeo.site.Model.Mission;
import igeo.site.Model.Room;
import igeo.site.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> joinRoom(RoomDto roomDto) {
        User user = userService.getAuthenticatedUserInfo();
        String roomId = roomDto.getRoomId();
        String password = roomDto.getPassword();
        //유저가 NULL일때
        if (user == null) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 접근입니다.");
        }
        //비밀번호 틀렸을 때
        if (!roomTracker.getRoom(roomId).getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 틀렸습니다.");
        }
        //방이 꽉찼을때
        if (roomTracker.getRoom(roomId).getCurrentUsersCount() == roomTracker.getRoom(roomId).getMaxUsers()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("방이 꽉 찼습니다.");
        }
        //방에 유저추가
        roomTracker.addUser(roomId, user);
        return ResponseEntity.ok().build();
    }
    //스킵투표
    public boolean addSkipVote(String roomId, String userName) {
        User user = userService.getUserByName(userName);
        return roomTracker.addSkipVote(roomId, user);
    }

    public int getRoomCount(String roomId) {
        return roomTracker.getRoom(roomId).getSkipVotesCount();
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
        //룸을 순회하면서 DTO로 변환
        for(Room room : rooms){
            RoomListDto temp = new RoomListDto();
            temp.setRoomId(room.getRoomId());
            temp.setType(room.getType());
            temp.setRoomName(room.getRoomName());
            temp.setOwner(room.getOwner());
            temp.setMaxUsers(room.getMaxUsers());
            temp.setCurrentUsers(room.getCurrentUsersCount());
            temp.setPlaying(room.isPlaying());
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
        //방장이 나갔을때
        if (roomTracker.getRoom(roomId).getOwner().equals(userName)) {
            roomTracker.changeOwner(roomId, userService.getUserById(roomTracker.getRoom(roomId).getCurrentUsers().iterator().next()));
        }
        //방에 유저가 없을때
        if (roomTracker.getRoom(roomId).getCurrentUsersCount() == 0) {
            roomTracker.deleteRoom(roomId);
        }
    }

    //방장 변경
    public void changeOwner(String roomId, String userName) {
        User user = userService.getUserByName(userName);
        roomTracker.changeOwner(roomId, user);
    }

    //미션 선택
    public void selectMission(String roomId, Long missionId) {
        roomTracker.selectMission(roomId, missionId);
    }

    public ResponseEntity<?> getRoomStatus(String roomId) {
        Room room = roomTracker.getRoom(roomId);
        List<RoomUserInfo> roomUserInfos = new ArrayList<>();
        //룸에 있는 유저들을 DTO로 변환
        for(Long userId : room.getCurrentUsers()){
            User user = userService.getUserById(userId);
            RoomUserInfo roomUserInfo = RoomUserInfo.builder()
                    .nickname(user.getName())
                    .character(user.getCharacter())
                    .level(user.getLevel())
                    .exp(user.getExp())
                    .nextExp(user.getNextExp())
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
                        .roomId(roomId)
                        .isPlaying(room.isPlaying())
                .build());
    }
    //룸 반환
    public Room getRoom(String roomId) {
        return roomTracker.getRoom(roomId);
    }

    //게임종료시 초기화
    public List<EndOfGameDto> endGame(String roomId) {
        roomTracker.getRoom(roomId).setPlaying(false);
        return userService.getEndOfGameDtos(roomId, roomTracker.getRoom(roomId));
    }

}
