package music.service;

import music.dto.request.RoleRequest;
import music.model.Role;

import java.util.List;

public interface RoleService {
    int addRole(RoleRequest request);
    Role getRole(int id);
    List<Role> getAllRole();
    int updateRole(int id, RoleRequest request);
    int deleteRole(int id);
    int addPermissionToRole(String roleName, String permissionName);
}
