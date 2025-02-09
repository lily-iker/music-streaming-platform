package music.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest implements Serializable {
    @Size(min = 8, message = "Username must be at least 8 characters")
    private String username;
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    @Size(min = 8, message = "Retype password must be at least 8 characters")
    private String retypePassword;
}
