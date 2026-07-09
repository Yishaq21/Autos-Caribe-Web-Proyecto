/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoscaribe.repository;

import com.autoscaribe.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
 *
 * @author Isaac
 */

// gestion de datos de los usuarios
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    // Consulta para buscar si está activo
    public List<Usuario> findByActivoTrue();
    
  
    public Usuario findByUsername(String username); // busca un usuario por su nombre de usuario
}