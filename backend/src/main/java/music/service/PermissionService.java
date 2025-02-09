package music.service;

import music.dto.request.PermissionRequest;
import music.model.Permission;

import java.util.List;

public interface PermissionService {
    int addPermission(PermissionRequest request);
    Permission getPermission(int id);
    List<Permission> getAllPermission();
    int updatePermission(int id, PermissionRequest request);
    int deletePermission(int id);
}
