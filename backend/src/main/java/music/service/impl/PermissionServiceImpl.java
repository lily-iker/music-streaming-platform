package music.service.impl;

import music.dto.request.PermissionRequest;
import music.exception.ResourceNotFoundException;
import music.model.Permission;
import music.repository.PermissionRepository;
import music.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public int addPermission(PermissionRequest request) {
        Permission permission = Permission.builder()
                .name(request.getName())
                .build();
        permissionRepository.save(permission);
        return permission.getId();
    }

    @Override
    public Permission getPermission(int id) {
        return getById(id);
    }

    @Override
    public List<Permission> getAllPermission() {
        return permissionRepository.findAll();
    }

    @Override
    @Transactional
    public int updatePermission(int id, PermissionRequest request) {
        Permission permission = getById(id);
        permission.setName(request.getName());
        return id;
    }

    @Override
    @Transactional
    public int deletePermission(int id) {
        permissionRepository.deleteById(id);
        return id;
    }

    private Permission getById(int id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
    }
}
