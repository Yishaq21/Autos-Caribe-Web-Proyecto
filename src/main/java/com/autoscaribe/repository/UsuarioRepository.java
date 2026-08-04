package com.autoscaribe.repository;

import com.autoscaribe.domain.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    public Optional<Usuario> findByUsernameAndActivoTrue(String username);

    public List<Usuario> findByActivoTrue();

    public Optional<Usuario> findByUsername(String username);

    public boolean existsByUsernameOrCorreo(String username, String correo);
}