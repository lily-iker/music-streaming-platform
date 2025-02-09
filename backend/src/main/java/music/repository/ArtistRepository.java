package music.repository;

import music.model.Artist;
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
import java.util.Set;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long>, JpaSpecificationExecutor<Artist> {
    Optional<Artist> findByName(String name);
    @Query("SELECT a FROM Artist a WHERE a.name IN :names")
    Set<Artist> findByNameIn(@Param("names") Set<String> names);
    @EntityGraph(attributePaths = {"albums"})
    @Query("SELECT a FROM Artist a " +
            "WHERE a.id = :id")
    Optional<Artist> findWithAlbums(@Param("id") Long id);
    @Query("SELECT a.id FROM Artist a")
    Page<Long> findAllIds(Pageable pageable);
    @Query("SELECT a FROM Artist a " +
            "WHERE a.id IN :ids")
    List<Artist> findAllByIdsAndSort(@Param("ids") List<Long> ids, Sort sort);
    @Query("SELECT a.id FROM Artist a " +
            "WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Long> findAllIdsByName(@Param("name") String name, Pageable pageable);
    @Query("SELECT a FROM Artist a " +
            "WHERE a.user.id = :userId")
    Optional<Artist> findByUserId(@Param("userId") Long userId);
    @Query(value = "SELECT a.id FROM artist a " +
            "ORDER BY a.followers DESC " +
            "LIMIT 6", nativeQuery = true)
    List<Long> find6MostFollowedArtistIds();
}
