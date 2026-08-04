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

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Transactional(readOnly = true)
    public List<Rol> getRoles() {
        return rolRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Rol> getRol(Integer idRol) {
        return rolRepository.findById(idRol);
    }

    @Transactional
    public void save(Rol rol) { // guarda y actualiza el rol en la BD
        rolRepository.save(rol);
    }

    @Transactional
    public void delete(Integer idRol) { // elimina el rol
        if (!rolRepository.existsById(idRol)) {
            throw new IllegalArgumentException("El rol con ID " + idRol + " no existe!");
        }
        try {
            rolRepository.deleteById(idRol);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el rol, tiene usuarios asociados");
        }
    }
}
