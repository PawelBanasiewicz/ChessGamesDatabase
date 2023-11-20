package chessGamesDatabase.service.impl;

import chessGamesDatabase.dao.RoleRepository;
import chessGamesDatabase.entity.Role;
import chessGamesDatabase.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role findRoleByRoleName(String roleName) {
        return roleRepository.findRoleByRoleName(roleName);
    }
}
