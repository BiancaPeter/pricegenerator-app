package com.spring.pricegenerator.repository;

import com.spring.pricegenerator.model.Role;
import com.spring.pricegenerator.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByRoleType(RoleType roleType);
}
