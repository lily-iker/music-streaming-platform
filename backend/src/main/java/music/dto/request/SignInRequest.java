package music.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest implements Serializable {
    @Size(min = 8, message = "Username must be at least 8 characters")
    private String username;
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
