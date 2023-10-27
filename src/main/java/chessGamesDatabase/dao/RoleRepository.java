package chessGamesDatabase.dao;

import chessGamesDatabase.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
