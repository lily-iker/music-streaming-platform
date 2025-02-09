package music.dto.response;


import music.model.Address;
import music.constant.Gender;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse implements Serializable {
    private String lastName;
    private String firstName;
    private Gender gender;
    private Date dateOfBirth;
    private String phoneNumber;
    private String email;
    private String username;
    private Set<Address> addresses;
}

