package org.faceview.user.service;

import org.faceview.user.entity.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService{
    Role getOneByAuthority(String name);
}
