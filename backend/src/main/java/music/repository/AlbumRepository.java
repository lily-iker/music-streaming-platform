package music.repository;

import music.model.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findByName(String name);
    @EntityGraph(attributePaths = {"artist", "songs"})
    @Query("SELECT a FROM Album a " +
            "WHERE a.id = :id")
    Optional<Album> findByIdWithAllFields(@Param("id") Long id);
    @Query("SELECT a.id FROM Album a")
    Page<Long> findAllIds(Pageable pageable);
    @EntityGraph(attributePaths = "artist")
    @Query("SELECT a FROM Album a " +
            "WHERE a.id IN :ids")
    List<Album> findAllByIdsAndSort(@Param("ids") List<Long> ids, Sort sort);
    @Query("SELECT a.id FROM Album a " +
            "WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Long> findAllIdsByName(@Param("name") String name, Pageable pageable);
}
