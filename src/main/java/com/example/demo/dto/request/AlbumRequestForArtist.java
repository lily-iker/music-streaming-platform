package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class AlbumRequestForArtist {
    @NotBlank(message = "Album's name can not be blank")
    private String name;
}
