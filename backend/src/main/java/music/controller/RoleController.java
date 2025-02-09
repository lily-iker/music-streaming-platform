package music.controller;

import music.dto.request.RoleRequest;
import music.dto.response.ApiResponse;
import music.model.Role;
import music.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
@Tag(name = "Role Controller")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Integer> addRole(@Valid @RequestBody RoleRequest request) {
        return new ApiResponse<>(HttpStatus.CREATED.value(),
                "Create role success",
                roleService.addRole(request));
    }

    @GetMapping("/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Role> getRole(@PathVariable @Min(1) int roleId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get role success",
                roleService.getRole(roleId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<Role>> getAllRoles() {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get all roles success",
                roleService.getAllRole());
    }

    @PutMapping("/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Integer> updateRole(@PathVariable @Min(1) int roleId,
                                           @Valid @RequestBody RoleRequest request) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Update role success",
                roleService.updateRole(roleId, request));
    }

    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Integer> deleteRole(@PathVariable @Min(1) int roleId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Delete role success",
                roleService.deleteRole(roleId));
    }

    @PostMapping("/add-permission")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Integer> addPermissionToRole(@RequestParam String roleName,
                                                    @RequestParam String permissionName) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Add permission to role success",
                roleService.addPermissionToRole(roleName, permissionName));
    }

}
