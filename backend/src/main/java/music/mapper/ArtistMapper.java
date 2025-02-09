package music.mapper;

import music.dto.response.ArtistResponse;
import music.dto.response.SearchArtistResponse;
import music.model.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    @Mapping(target = "songs", ignore = true)
    ArtistResponse toArtistResponse(Artist artist);
    SearchArtistResponse toSearchArtistResponse(Artist artist);
}
