package chessGamesDatabase.service;

import chessGamesDatabase.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAllRoles();

    Role findRoleByRoleName(String roleName);
}
