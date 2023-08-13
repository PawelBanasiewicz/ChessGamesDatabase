package chessGamesDatabase.service;

import chessGamesDatabase.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAll();
    List<Role> findByIdIn(List<Long> roleIds);
}
