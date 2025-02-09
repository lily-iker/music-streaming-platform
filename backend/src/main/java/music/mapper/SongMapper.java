package music.mapper;

import music.dto.request.SongRequest;
import music.dto.request.UpdateSongRequest;
import music.dto.response.SearchSongResponse;
import music.dto.response.SongResponse;
import music.model.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SongMapper {
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "artists", ignore = true)
    @Mapping(target = "album", ignore = true)
    Song toSong(SongRequest request);
    @Mapping(target = "genres", ignore = true)
    void updateSong(@MappingTarget Song song, UpdateSongRequest request);
    SongResponse toSongResponse(Song song);
    SearchSongResponse toSearchSongResponse(Song song);
}
