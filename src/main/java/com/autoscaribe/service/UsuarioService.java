/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoscaribe.service;
import com.autoscaribe.domain.Usuario;
import com.autoscaribe.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author Isaac
 */
@Service // gestion de usuarios
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)  //Retorna lista de usuarios
    public List<Usuario> getUsuarios(boolean activo) {
        if (activo) {
            return usuarioRepository.findByActivoTrue();
        }
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true) //Busca un usuario por su id
    public Optional<Usuario> getUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    @Transactional //Guarda o actualiza un usuario
    public void save(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void delete(Integer idUsuario) { //elimina un usuario
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new IllegalArgumentException("El usuario con ID " + idUsuario + " no existe!");
        }
        try {
            usuarioRepository.deleteById(idUsuario);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el usuario, tiene información asociada");
        }
    }
    @Transactional(readOnly = true)
    public Usuario getUsuarioPorUsername(String username) {
       
        return usuarioRepository.findByUsername(username);
    }
}