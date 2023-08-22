package chessGamesDatabase.service;

import chessGamesDatabase.dao.RoleRepository;
import chessGamesDatabase.entity.Role;
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
    public List<Role> findRolesByIdIn(List<Long> roleIds) {
        return roleRepository.findByRoleIdIn(roleIds);
    }

}
