package music.controller;

import music.dto.request.UpdateInfoRequest;
import music.dto.request.UserRequest;
import music.dto.response.ApiResponse;
import music.dto.response.UserInfoResponse;
import music.model.User;
import music.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User Controller")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Add user", description = "Add new user")
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> addUser(@Valid @RequestBody UserRequest request) {
        return new ApiResponse<>(HttpStatus.CREATED.value(),
                "User added",
                userService.addUser(request));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<User> getUser(@PathVariable long userId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get User " + userId + " successful",
                userService.getUser(userId));
    }

    @GetMapping("/my-info")
    public ApiResponse<UserInfoResponse> getMyInfo() {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "My Info",
                userService.getMyInfo());
    }

    @PutMapping("/update-my-info")
    public ApiResponse<Long> updateMyInfo(@Valid @RequestBody UpdateInfoRequest request) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Update My Info success",
                userService.updateMyInfo(request));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> getAllUser(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(defaultValue = "id:asc", required = false) String sortBy) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "All users",
                userService.getAllUsers(pageNo, pageSize, sortBy));
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> updateUser(@PathVariable long userId,
                                        @Valid @RequestBody UserRequest request){
        return new ApiResponse<>(HttpStatus.OK.value(),
                "User " + userId + " updated",
                userService.updateUser(userId, request));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> deleteUser(@PathVariable long userId) {
        return new ApiResponse<>(HttpStatus.NO_CONTENT.value(),
                "User " + userId + " deleted!",
                userService.deleteUser(userId));
    }

    @PostMapping("/add-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> addRoleToUser(@RequestParam String username,
                                        @RequestParam String roleName) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                roleName + " added to " + username,
                userService.addRoleToUser(username, roleName));
    }

    @DeleteMapping("/delete-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> deleteRoleFromUser(@RequestParam String username,
                                             @RequestParam String roleName) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                roleName + " deleted from " + username,
                userService.deleteRoleFromUser(username, roleName));
    }

    @GetMapping("/sort-and-search-criteria")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> sortAndSearchCriteria(
            @RequestParam(defaultValue = "0", required = false) int offset,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "id:asc", required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "") String... search) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Criteria Search",
                userService.sortAndCriteriaSearch(offset, pageSize, sortBy, search));
    }

    @GetMapping("/specification")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> sortAndSearchSpecification(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "id:asc", required = false) String sortBy,
            @RequestParam(defaultValue = "id!=0", required = false) String search) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Specification Search",
                userService.sortAndSpecificationSearch(pageNo, pageSize, sortBy, search));
    }
}

