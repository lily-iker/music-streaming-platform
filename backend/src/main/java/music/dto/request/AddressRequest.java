package music.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest implements Serializable {
    @NotBlank(message = "Invalid street number")
    private String streetNumber;

    @NotBlank(message = "Invalid street")
    private String street;

    @NotBlank(message = "Invalid city")
    private String city;

    @NotBlank(message = "Invalid country")
    private String country;
}
