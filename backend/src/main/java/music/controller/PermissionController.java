package music.controller;

import music.dto.request.PermissionRequest;
import music.dto.response.ApiResponse;
import music.model.Permission;
import music.service.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
@Tag(name = "Permission Controller")
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping("")
    public ApiResponse<Integer> addPermission(@Valid @RequestBody PermissionRequest request) {
        return new ApiResponse<>(HttpStatus.CREATED.value(),
                "Create permission success",
                permissionService.addPermission(request));
    }

    @GetMapping("/{permissionId}")
    public ApiResponse<Permission> getPermission(@PathVariable @Min(1) int permissionId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get permission success",
                permissionService.getPermission(permissionId));
    }

    @GetMapping("/all")
    public ApiResponse<List<Permission>> getAllPermissions() {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get all permissions success",
                permissionService.getAllPermission());
    }

    @PutMapping("/{permissionId}")
    public ApiResponse<Integer> updatePermission(@PathVariable @Min(1) int permissionId,
                                                 @Valid @RequestBody PermissionRequest request) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Update permission success",
                permissionService.updatePermission(permissionId, request));
    }

    @DeleteMapping("/{permissionId}")
    public ApiResponse<Integer> deletePermission(@PathVariable @Min(1) int permissionId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Delete permission success",
                permissionService.deletePermission(permissionId));
    }
}
