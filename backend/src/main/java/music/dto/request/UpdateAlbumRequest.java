package music.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor  // Default constructor for Jackson deserialization
@AllArgsConstructor // Constructor for all fields
public class UpdateAlbumRequest implements Serializable {
    @NotBlank(message = "New Album's name can not be blank")
    private String name;
}
