package igeo.site.Model;

import igeo.site.DTO.CreateRoomDto;
import lombok.Data;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Room {
    private CreateRoomDto.RoomType type;
    private String roomId;
    private String roomName;
    private String owner;
    private String password;
    private int maxUsers;
    private Set<Long> currentUsers = ConcurrentHashMap.newKeySet();
    private Set<Long> skipVotes = ConcurrentHashMap.newKeySet();
    private Long MissionId;

    public Room(CreateRoomDto createRoomDto, String roomId) {
        this.type = createRoomDto.getType();
        this.roomName = createRoomDto.getRoomName();
        this.roomId = roomId;
        this.owner = createRoomDto.getSender();
        this.password = createRoomDto.getPassword();
        this.maxUsers = createRoomDto.getMaxUser();
        this.MissionId = null;
    }
    //방에 유저 추가
    public void addUser(Long userId) {
        if (currentUsers.size() < maxUsers && !currentUsers.contains(userId)){
            currentUsers.add(userId);
        }else{
            throw new IllegalStateException("방이 가득찼습니다");
        }
    }
    //스킵투표
    public boolean addSkipVote(Long userId) {
        // 이미 투표했는지 검사
        if (!skipVotes.contains(userId) && currentUsers.contains(userId)) {
            skipVotes.add(userId);
            int RequireSkipVote = skipVoteCount(currentUsers.size());
            return skipVotes.size() > RequireSkipVote;
        }
        return false;
    }
    //스킵 갯수 설정
    public int skipVoteCount(int count){
        if(count <= 2){
            return count;
        }else{
            return count-1;
        }
    }
    //스킵투표 초기화
    public void clearSkipVotes() {
        skipVotes.clear();
    }
    //방장 스킵
    public boolean ownerSkipVote(Long userId){
        return userId.toString().equals(owner);
    }

    public void removeUser(Long userId) {
        currentUsers.remove(userId);
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    public boolean isFull() {
        return currentUsers.size() == maxUsers;
    }

    public Long getCurrentUsersCount() {
        return (long) currentUsers.size();
    }
}
