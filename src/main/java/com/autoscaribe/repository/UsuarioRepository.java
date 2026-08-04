package com.autoscaribe.repository;

import com.autoscaribe.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    public List<Usuario> findByActivoTrue();

    public Usuario findByUsername(String username); // busca un usuario por su nombre de usuario
}
