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

// Servicio para gestionar usuarios
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    // Inyecta los repositorios y el codificador de contraseñas
    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Trae usuarios (activos o todos)
    @Transactional(readOnly = true)
    public List<Usuario> getUsuarios(boolean activo) {
        return activo ? usuarioRepository.findByActivoTrue() : usuarioRepository.findAll();
    }

    // Trae un usuario por su id
    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    // Trae un usuario por username
    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    // Registro de cliente (desde el formulario público)
    @Transactional
    public void registrar(Usuario usuario) {
        // Verifica que no exista el username o correo
        if (usuarioRepository.existsByUsernameOrCorreo(usuario.getUsername(), usuario.getCorreo())) {
            throw new DataIntegrityViolationException("El usuario o correo ya está en uso.");
        }

        // Encripta la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivo(true);

        // Asigna el rol CLIENTE por defecto
        Rol rolCliente = rolRepository.findByRol("CLIENTE")
                .orElseThrow(() -> new IllegalStateException("El rol CLIENTE no existe. Créalo primero en /rol/listado."));
        usuario.getRoles().add(rolCliente);

        // Guarda el usuario
        usuarioRepository.save(usuario);
    }

    // Guarda o actualiza un usuario (desde el admin)
    @Transactional
    public void save(Usuario usuario) {
        if (usuario.getIdUsuario() == null) {
            // Usuario nuevo: la contraseña es obligatoria
            if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
                throw new IllegalArgumentException("La contraseña es obligatoria para nuevos usuarios.");
            }
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        } else if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            // Editando y dejaron password vacío: se mantiene el actual
            Usuario existente = usuarioRepository.findById(usuario.getIdUsuario())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario a modificar no encontrado."));
            usuario.setPassword(existente.getPassword());
        } else {
            // Editando y pusieron password nuevo: se encripta
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        usuarioRepository.save(usuario);
    }

    // Desactiva un usuario 
    @Transactional
    public void desactivar(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario con ID " + idUsuario + " no existe."));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    // Actualiza los datos del perfil del usuario logueado
    @Transactional
    public void actualizarPerfil(Integer idUsuario, String nombre, String apellidos, String correo, String telefono) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        // Verifica que el correo no este en uso por otro
        if (!usuario.getCorreo().equalsIgnoreCase(correo)
                && usuarioRepository.existsByUsernameOrCorreo(usuario.getUsername(), correo)) {
            throw new DataIntegrityViolationException("Ese correo ya está en uso por otra cuenta.");
        }

        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setCorreo(correo);
        usuario.setTelefono(telefono);
        usuarioRepository.save(usuario);
    }

    // Actualiza los roles de un usuario
    @Transactional
    public void actualizarRoles(Integer idUsuario, java.util.List<Integer> idRoles) {
        // Debe tener al menos un rol
        if (idRoles == null || idRoles.isEmpty()) {
            throw new IllegalArgumentException("El usuario debe tener al menos un rol asignado.");
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        // Limpia los roles actuales
        usuario.getRoles().clear();

        // Agrega los roles seleccionados
        for (Integer idRol : idRoles) {
            Rol rol = rolRepository.findById(idRol)
                    .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado."));
            usuario.getRoles().add(rol);
        }

        usuarioRepository.save(usuario);
    }
}