package music.dto.response;

import music.constant.Gender;
import music.model.Address;
import music.model.Role;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@Builder
public class UserDetailsResponse implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Date dateOfBirth;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
    private Boolean active;
    private Set<Address> addresses;
    private Set<Role> roles;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private Boolean enabled;
}
