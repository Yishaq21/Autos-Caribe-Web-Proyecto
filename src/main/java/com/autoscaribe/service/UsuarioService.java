package com.autoscaribe.service;

import com.autoscaribe.domain.Rol;
import com.autoscaribe.domain.Usuario;
import com.autoscaribe.repository.RolRepository;
import com.autoscaribe.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // gestion de usuarios
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Usuario> getUsuarios(boolean activo) {
        return activo ? usuarioRepository.findByActivoTrue() : usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    // Se usa cuando un cliente se registra por su cuenta 
    // Automáticamente le asigna el rol CLIENTE.
    @Transactional
    public void registrar(Usuario usuario) {
        if (usuarioRepository.existsByUsernameOrCorreo(usuario.getUsername(), usuario.getCorreo())) {
            throw new DataIntegrityViolationException("El usuario o correo ya está en uso.");
        }
        
        // 1. Se encripta el password antes de guardar 
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivo(true);

        // 2. Le asigna el rol CLIENTE por defecto ANTES de hacer el save
        Rol rolCliente = rolRepository.findByRol("CLIENTE")
                .orElseThrow(() -> new IllegalStateException("El rol CLIENTE no existe. Créalo primero en /rol/listado."));
        usuario.getRoles().add(rolCliente);
        
        // 3. Se guarda en la base de datos una única vez con sus roles ya cargados
        usuarioRepository.save(usuario);
    }

    // Se usa cuando el administrador crea o edita un usuario desde /usuario/guardar
    @Transactional
    public void save(Usuario usuario) {
        if (usuario.getIdUsuario() == null) {
            // Usuario nuevo: el password es obligatorio y se encripta
            if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
                throw new IllegalArgumentException("La contraseña es obligatoria para nuevos usuarios.");
            }
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        } else if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            // Se está editando y dejaron el password en blanco: se conserva el que ya tenía
            Usuario existente = usuarioRepository.findById(usuario.getIdUsuario())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario a modificar no encontrado."));
            usuario.setPassword(existente.getPassword());
        } else {
            // Se está editando y sí escribieron un password nuevo: se encripta
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void delete(Integer idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new IllegalArgumentException("El usuario con ID " + idUsuario + " no existe!");
        }
        try {
            usuarioRepository.deleteById(idUsuario);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el usuario, tiene información asociada");
        }
    }
}