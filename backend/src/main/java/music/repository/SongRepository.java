package music.repository;

import music.model.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long>, JpaSpecificationExecutor<Song> {
    boolean existsByName(String name);
    @EntityGraph(attributePaths = {"genres", "artists", "album"})
    @Query("SELECT s FROM Song s " +
            "WHERE s.id = :id")
    Optional<Song> findByIdWithAllFields(@Param("id") Long id);
    @Query("SELECT s.id FROM Song s")
    Page<Long> findAllIds(Pageable pageable);
    @EntityGraph(attributePaths = {"genres", "artists", "album"})
    @Query("SELECT s FROM Song s " +
            "WHERE s.id IN :ids")
    List<Song> findAllByIdsAndSort(@Param("ids") List<Long> ids, Sort sort);
    @Query("SELECT s.id FROM Song s " +
            "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Long> findAllIdsByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT s.id FROM Song s JOIN s.genres g WHERE g.id = :genreId")
    Page<Long> findAllIdsByGenre(@Param("genreId") Integer genreId, Pageable pageable);

    @Query("SELECT DISTINCT s FROM Song s JOIN FETCH s.artists a " +
            "WHERE s.id IN (SELECT s2.id FROM Song s2 JOIN s2.artists a2 WHERE a2.id = :artistId) " +
            "ORDER BY s.likeCount DESC")
    List<Song> findAllByArtistId(@Param("artistId") Long artistId);
    @Query(value = "SELECT s.id FROM song s " +
            "ORDER BY RAND() " +
            "LIMIT 6", nativeQuery = true)
    List<Long> find6RandomSongIds();

    @Query(value = "SELECT s.id FROM song s " +
            "ORDER BY s.like_count DESC " +
            "LIMIT 6", nativeQuery = true)
    List<Long> find6MostLikedSongIds();

    @Query("SELECT DISTINCT s FROM Song s " +
            "LEFT JOIN FETCH s.artists " +
            "WHERE s.id IN :ids " +
            "ORDER BY s.likeCount DESC")
    List<Song> findSongsWithArtistsByIds(List<Long> ids);
}
