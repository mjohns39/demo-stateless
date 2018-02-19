package com.demo.authserver.user.repo;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.authserver.user.model.Privilege;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

	public Privilege findByName(String name);
}
