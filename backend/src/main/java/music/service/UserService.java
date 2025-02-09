package music.service;

import music.dto.request.UpdateInfoRequest;
import music.dto.request.UserRequest;
import music.dto.response.PageResponse;
import music.dto.response.PageResponseCriteria;
import music.dto.response.UserInfoResponse;
import music.model.User;

public interface UserService {
    long addUser(UserRequest request);
    User getUser(Long id);
    long updateUser(Long id, UserRequest request);
    long deleteUser(Long id);
    UserInfoResponse getMyInfo();
    long updateMyInfo(UpdateInfoRequest request);
    PageResponse<?> getAllUsers(int pageNo, int pageSize, String sortBy);
    long addRoleToUser(String username, String roleName);
    long deleteRoleFromUser(String username, String roleName);
    PageResponseCriteria<?> sortAndCriteriaSearch(int offset, int pageSize, String sortBy, String... search);
    PageResponse<?> sortAndSpecificationSearch(int pageNo, int pageSize, String sortBy, String search);
}
