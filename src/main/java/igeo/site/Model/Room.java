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

    public Room(CreateRoomDto createRoomDto, String roomId) {
        this.type = createRoomDto.getType();
        this.roomName = createRoomDto.getRoomName();
        this.roomId = roomId;
        this.owner = createRoomDto.getSender();
        this.password = createRoomDto.getPassword();
        this.maxUsers = createRoomDto.getMaxUser();
    }

    public void addUser(Long userId) {
        if (currentUsers.size() < maxUsers && !currentUsers.contains(userId)){
            currentUsers.add(userId);
        }else{
            throw new IllegalStateException("방이 가득찼습니다");
        }
    }

    public int getCurrentUsers() {
        return currentUsers.size();
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
}
