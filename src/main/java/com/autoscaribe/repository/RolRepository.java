package com.autoscaribe.repository;

import com.autoscaribe.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

}
