package org.faceview.user.service;

import org.faceview.user.entity.Role;
import org.faceview.user.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getOneByAuthority(String name) {
        return this.roleRepository.getRoleByAuthority(name);
    }
}
