package org.faceview.user.repository;

import org.faceview.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role getRoleByAuthority(String authority);
}
