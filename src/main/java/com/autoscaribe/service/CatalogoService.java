package com.autoscaribe.service;

import com.autoscaribe.domain.Vehiculo;
import com.autoscaribe.repository.VehiculoRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CatalogoService {

    private final VehiculoRepository vehiculoRepository;

    public CatalogoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> listarTodos() {
        return vehiculoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> buscarPorMarca(String marca) {
        if (marca == null || marca.isBlank()) {
            return listarTodos();
        }
        return vehiculoRepository
                .findByMarcaContainingIgnoreCaseOrderByMarcaAsc(marca.trim());
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> buscarPorCategoria(String categoria) {
        if (categoria == null || categoria.isBlank()) {
            return listarTodos();
        }
        return vehiculoRepository
                .findByCategoriaIgnoreCaseOrderByMarcaAsc(categoria.trim());
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> buscarPorPrecio(BigDecimal precioMinimo,
            BigDecimal precioMaximo) {
        if (precioMinimo == null || precioMaximo == null) {
            return listarTodos();
        }
        return vehiculoRepository
                .findByPrecioBetweenOrderByPrecioAsc(precioMinimo, precioMaximo);
    }

    @Transactional(readOnly = true)
    public Optional<Vehiculo> buscarPorId(Integer idVehiculo) {
        return vehiculoRepository.findById(idVehiculo);
    }
}
