package com.autoscaribe.repository;

import com.autoscaribe.domain.Rol;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
a
    public Optional<Rol> findByRol(String rol);
}
