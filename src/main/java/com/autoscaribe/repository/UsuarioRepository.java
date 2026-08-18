package com.autoscaribe.repository;

import com.autoscaribe.domain.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Busca usuario activo por username (para el login)
    public Optional<Usuario> findByUsernameAndActivoTrue(String username);

    // Trae solo los usuarios activos
    public List<Usuario> findByActivoTrue();

    // Busca usuario por username (activo o no)
    public Optional<Usuario> findByUsername(String username);

    // Verifica si ya existe el username o el correo
    public boolean existsByUsernameOrCorreo(String username, String correo);
}