package com.example.demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Builder
public class SongRequestForArtist {
    @NotBlank(message = "Last name can not be blank!")
    private String name;

    @Min(value = 60, message = "Song's duration must be at least 1 minutes")
    private Integer duration;

    @NotEmpty(message = "Song's genre must have at least 1")
    private Set<GenreRequest> genres;

    @NotEmpty(message = "Song's artist must have at least 1")
    private Set<ArtistRequest> collaborateArtists;

    private AlbumRequest album;
}
