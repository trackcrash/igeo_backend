package igeo.site.Service;


import igeo.site.DTO.UserPermissionDto;
import igeo.site.Model.User;
import igeo.site.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserService userService;
    private final UserRepository userRepository;

    public void UserPermissionChange(UserPermissionDto userPermissionDto)
    {
        User thisuser = userService.getAuthenticatedUserInfo();
        if(thisuser.getPermissions() != 2) return;
        User user = userService.getUserByName(userPermissionDto.getUserName());

        if(user.getPermissions() == userPermissionDto.getPermissionNum()) return;
        user.setPermissions(userPermissionDto.getPermissionNum());
        userRepository.save(user);
    }
}
