package music.dto.request;

import music.constant.Gender;
import music.validator.gender.GenderValid;
import music.validator.phone.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest implements Serializable {

    @NotBlank(message = "Last name can not be blank!")
    @Pattern(regexp = "^[a-zA-Z]{1,20}$", message = "Last name can only be between 1 to 20 alphabet characters")
    private String lastName;

    @NotBlank(message = "First name can not be blank!")
    @Pattern(regexp = "^[a-zA-Z]{1,20}$", message = "First name can only be between 1 to 20 alphabet characters")
    private String firstName;

    @GenderValid(message = "Gender can only be MALE|FEMALE|OTHER")
    private Gender gender;

    @NotNull(message = "Date of birth can not be blank")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfBirth;

    @PhoneNumber(message = "Phone number invalid!")
    private String phoneNumber;

    @Email(message = "Email invalid!")
    private String email;

    @Size(min = 8, message = "Username must be at least 8 characters")
    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotNull(message = "User's status can not be null")
    private Boolean active;

    @NotEmpty(message = "Roles can not be empty")
    private Set<RoleRequest> roles;

    private Boolean accountNonExpired;

    private Boolean accountNonLocked;

    private Boolean credentialsNonExpired;

    private Boolean enabled;

}

