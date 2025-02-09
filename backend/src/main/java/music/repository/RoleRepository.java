package music.repository;

import music.constant.RoleName;
import music.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);
    @Query("SELECT r FROM Role r WHERE r.name IN :names")
    Set<Role> findByNameIn(@Param("names") Set<RoleName> names);
    @Query("SELECT DISTINCT r FROM Role r " +
            "LEFT JOIN FETCH r.users " +
            "WHERE r.name = :name")
    Optional<Role> findByNameWithUsers(@Param("name") RoleName name);
}
