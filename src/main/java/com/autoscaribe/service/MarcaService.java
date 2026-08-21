package com.autoscaribe.service;

import com.autoscaribe.domain.Marca;
import com.autoscaribe.repository.MarcaRepository;
import com.autoscaribe.repository.VehiculoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarcaService {

    private final MarcaRepository marcaRepository;
    private final VehiculoRepository vehiculoRepository;

    // Inyecta el repositorio de marcas y el de vehículos
    public MarcaService(MarcaRepository marcaRepository, VehiculoRepository vehiculoRepository) {
        this.marcaRepository = marcaRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    // Trae todas las marcas, ordenadas alfabéticamente
    @Transactional(readOnly = true)
    public List<Marca> getMarcas() {
        return marcaRepository.findAllByOrderByMarcaAsc();
    }

    // Trae una marca por su id
    @Transactional(readOnly = true)
    public Optional<Marca> getMarca(Integer idMarca) {
        return marcaRepository.findById(idMarca);
    }

    // Guarda o actualiza una marca
    @Transactional
    public void save(Marca marca) {
        marcaRepository.save(marca);
    }

    // Elimina una marca
    @Transactional
    public void delete(Integer idMarca) {
        Optional<Marca> marcaOpt = marcaRepository.findById(idMarca);

        // Verifica que la marca exista
        if (marcaOpt.isEmpty()) {
            throw new IllegalArgumentException("La marca con ID " + idMarca + " no existe!");
        }

        // No se puede borrar si ya hay vehículos registrados con esa marca
        if (vehiculoRepository.existsByMarcaIgnoreCase(marcaOpt.get().getMarca())) {
            throw new IllegalStateException(
                    "No se puede eliminar la marca, tiene vehículos asociados en el catálogo");
        }

        try {
            marcaRepository.deleteById(idMarca);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar la marca, tiene información asociada");
        }
    }
}
