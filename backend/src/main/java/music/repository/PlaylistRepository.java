package music.repository;

import music.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Query("SELECT p FROM Playlist p " +
            "JOIN FETCH p.user u " +
            "JOIN FETCH p.songs s " +
            "JOIN FETCH s.artists a " +
            "WHERE p.id = :id")
    Optional<Playlist> findByIdWithAllFields(@Param("id") Long id);
}
