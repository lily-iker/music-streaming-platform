package music.mapper;

import music.dto.request.RegisterRequestForArtist;
import music.dto.request.UpdateInfoRequest;
import music.dto.request.UserRequest;
import music.dto.response.UserDetailsResponse;
import music.dto.response.UserInfoResponse;
import music.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserRequest request);
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserRequest request);
    UserInfoResponse toUserInfoResponse(User user);
    UserDetailsResponse toUserDetailsResponse(User user);
    void updateMyInfo(@MappingTarget User user, UpdateInfoRequest request);
    User toUser(RegisterRequestForArtist request);
}
