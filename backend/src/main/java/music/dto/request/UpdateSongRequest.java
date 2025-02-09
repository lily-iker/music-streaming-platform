package music.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Builder
public class UpdateSongRequest implements Serializable {

    @NotBlank(message = "New Song's name can not be blank!")
    private String name;

    @Min(value = 60, message = "Song's duration must be at least 1 minutes")
    private Integer duration;

    @NotEmpty(message = "Song's genre must have at least 1")
    private Set<String> genreNames;
}
