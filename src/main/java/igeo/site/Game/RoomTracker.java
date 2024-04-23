package igeo.site.Game;

import igeo.site.DTO.CreateRoomDto;
import igeo.site.DTO.RoomDto;
import igeo.site.Model.Room;
import igeo.site.Model.User;
import igeo.site.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
//방 생성 및 추적을 위한 클래스
@Component
public class RoomTracker {

    private final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();
    private final AtomicInteger roomCounter = new AtomicInteger(0);

    //방 생성
    //방의 id는 roomCounter를 이용하여 생성
    public Room createRoom(CreateRoomDto createRoomDto, User user) {
        String roomId = String.valueOf(roomCounter.getAndIncrement());
        Room room = new Room(createRoomDto, roomId);
        addUser(roomId, user);
        rooms.put(roomId, room);
        return room;
    }

    //방에 유저 추가
    public boolean addUser(String roomId, User user) {
        if(rooms.containsKey(roomId)) {
            rooms.get(roomId).addUser(user.getId());
            return true;
        } else {
            return false;
        }
    }

    //방장 변경
    public void changeOwner(String roomId, User user) {
        rooms.get(roomId).setOwner(user.getId().toString());
    }

    //스킵투표
    public boolean addSkipVote(String roomId, User user) {
        Room room = rooms.get(roomId);
        if(room != null) {
            boolean isSkipped = room.addSkipVote(user.getId());
            if(isSkipped) {
                room.clearSkipVotes();
                return true;
            }
        }
        return false;
    }

    //게임시작 상태변경
    public void startGame(String roomId) {
        rooms.get(roomId).setPlaying(true);
    }

    //방장 스킵
    public boolean ownerSkipVote(String roomId, User user) {
        if(rooms.containsKey(roomId)) {
            return rooms.get(roomId).ownerSkipVote(user.getId());
        } else {
            return false;
        }
    }


    //퇴장
    public void leaveRoom(String roomId, User user) {
        rooms.get(roomId).removeUser(user.getId());
    }

    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }

    //방 삭제
    public void deleteRoom(String roomId) {
        rooms.remove(roomId);
    }

    //방 목록 반환
    public List<Room> getRooms() {
        return rooms.values().stream().toList();
    }

    //미션 선택
    public void selectMission(String roomId, Long missionId) {
        Room room = rooms.get(roomId);
        room.setMissionId(missionId);
        rooms.put(roomId, room);
    }
}
