package com.autoscaribe.service;

import com.autoscaribe.domain.Rol;
import com.autoscaribe.repository.RolRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolService {

    private final RolRepository rolRepository;

    // Inyecta el repositorio de roles
    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    // Trae todos los roles
    @Transactional(readOnly = true)
    public List<Rol> getRoles() {
        return rolRepository.findAll();
    }

    // Trae un rol por su id
    @Transactional(readOnly = true)
    public Optional<Rol> getRol(Integer idRol) {
        return rolRepository.findById(idRol);
    }

    // Guarda o actualiza un rol
    @Transactional
    public void save(Rol rol) {
        rolRepository.save(rol);
    }

    // Elimina un rol
    @Transactional
    public void delete(Integer idRol) {
        // Verifica que el rol exista
        if (!rolRepository.existsById(idRol)) {
            throw new IllegalArgumentException("El rol con ID " + idRol + " no existe!");
        }

        try {
            rolRepository.deleteById(idRol);
        } catch (DataIntegrityViolationException e) {
            // No se puede borrar si tiene usuarios asociados
            throw new IllegalStateException("No se puede eliminar el rol, tiene usuarios asociados");
        }
    }
}
