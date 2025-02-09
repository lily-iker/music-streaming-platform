package music.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class AlbumRequest implements Serializable {
    @NotBlank(message = "Album's name can not be blank")
    private String name;
    @NotBlank(message = "Artist's name can not be blank")
    private String artistName;
}
