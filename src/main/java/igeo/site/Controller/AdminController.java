package igeo.site.Controller;

import igeo.site.DTO.UserPermissionDto;
import igeo.site.Service.AdminService;
import igeo.site.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    @PostMapping("/userPermissionChange")
    public void userPermissionChange(@RequestBody UserPermissionDto userPermissionDto)
    {
        adminService.UserPermissionChange(userPermissionDto);
    }
}
