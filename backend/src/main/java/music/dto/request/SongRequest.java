package music.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Builder
public class SongRequest implements Serializable {

    @NotBlank(message = "Song name can not be blank!")
    private String name;

    @Min(value = 60, message = "Song's duration must be at least 1 minutes")
    private Integer duration;

    @NotEmpty(message = "Song's genre must have at least 1")
    private Set<String> genreNames;

    @NotEmpty(message = "Song's artist must have at least 1")
    private Set<String> artistNames;

    private String albumName;
}
