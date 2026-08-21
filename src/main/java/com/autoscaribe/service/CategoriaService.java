package com.autoscaribe.service;

import com.autoscaribe.domain.Categoria;
import com.autoscaribe.repository.CategoriaRepository;
import com.autoscaribe.repository.VehiculoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final VehiculoRepository vehiculoRepository;

    // Inyecta el repositorio de categorías y el de vehículos
    public CategoriaService(CategoriaRepository categoriaRepository, VehiculoRepository vehiculoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    // Trae todas las categorías, ordenadas alfabéticamente
    @Transactional(readOnly = true)
    public List<Categoria> getCategorias() {
        return categoriaRepository.findAllByOrderByCategoriaAsc();
    }

    // Trae una categoría por su id
    @Transactional(readOnly = true)
    public Optional<Categoria> getCategoria(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }

    // Guarda o actualiza una categoría
    @Transactional
    public void save(Categoria categoria) {
        categoriaRepository.save(categoria);
    }

    // Elimina una categoría
    @Transactional
    public void delete(Integer idCategoria) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(idCategoria);

        // Verifica que la categoría exista
        if (categoriaOpt.isEmpty()) {
            throw new IllegalArgumentException("La categoría con ID " + idCategoria + " no existe!");
        }

        // No se puede borrar si ya hay vehículos registrados con esa categoría
        if (vehiculoRepository.existsByCategoriaIgnoreCase(categoriaOpt.get().getCategoria())) {
            throw new IllegalStateException(
                    "No se puede eliminar la categoría, tiene vehículos asociados en el catálogo");
        }

        try {
            categoriaRepository.deleteById(idCategoria);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar la categoría, tiene información asociada");
        }
    }
}
