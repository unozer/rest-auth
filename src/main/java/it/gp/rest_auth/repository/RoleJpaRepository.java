package it.gp.rest_auth.repository;

import it.gp.rest_auth.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {

    RoleEntity findByName(String name);
}
