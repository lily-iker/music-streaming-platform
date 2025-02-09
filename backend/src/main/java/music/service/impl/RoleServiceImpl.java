package music.service.impl;

import music.constant.PermissionName;
import music.constant.RoleName;
import music.dto.request.RoleRequest;
import music.exception.ResourceNotFoundException;
import music.model.Permission;
import music.model.Role;
import music.repository.PermissionRepository;
import music.repository.RoleRepository;
import music.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public int addRole(RoleRequest request) {
        Role role = Role.builder()
                .name(request.getName())
                .build();
        roleRepository.save(role);
        return role.getId();
    }

    @Override
    public Role getRole(int id) {
        return getById(id);
    }

    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public int updateRole(int id, RoleRequest request) {
        Role role = getById(id);
        role.setName(request.getName());
        return id;
    }

    @Override
    @Transactional
    public int deleteRole(int id) {
        roleRepository.deleteById(id);
        return id;
    }

    @Override
    @Transactional
    public int addPermissionToRole(String roleName, String permissionName) {
        Role role = roleRepository.findByName(RoleName.valueOf(roleName))
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        Permission permission = permissionRepository.findByName(PermissionName.valueOf(permissionName))
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));

        savePermissionToRole(permission, role);

        return role.getId();
    }

    private Role getById(int id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    private void savePermissionToRole(Permission permission, Role role) {
        if (role.getPermissions() == null) {
            role.setPermissions(new HashSet<>());
        }
        if (permission.getRoles() == null) {
            permission.setRoles(new HashSet<>());
        }
        role.getPermissions().add(permission);
        permission.getRoles().add(role);
    }
}