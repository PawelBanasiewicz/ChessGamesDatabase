package chessGamesDatabase.dao;

import chessGamesDatabase.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByRoleIdIn(List<Long> roleIds);
}
