package music.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
@Builder
public class UpdateArtistInfoRequest implements Serializable {
    @Size(max = 2048, message = "Artist's bio should be between 0 and 2048 characters")
    private String bio;
}
