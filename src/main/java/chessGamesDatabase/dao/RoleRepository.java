package chessGamesDatabase.dao;

import chessGamesDatabase.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
    List<Role> findByRoleIdIn(List<Long> roleIds);
}
