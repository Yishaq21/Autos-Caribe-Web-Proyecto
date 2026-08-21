package com.autoscaribe.repository;

import com.autoscaribe.domain.Vehiculo;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {

    List<Vehiculo> findByMarcaContainingIgnoreCaseOrderByMarcaAsc(String marca);

    List<Vehiculo> findByCategoriaIgnoreCaseOrderByMarcaAsc(String categoria);

    List<Vehiculo> findByPrecioBetweenOrderByPrecioAsc(BigDecimal precioMinimo,
            BigDecimal precioMaximo);

    // Indica si existe algún vehículo registrado con esa marca (para no dejar
    // eliminar una marca del catálogo mientras siga en uso)
    boolean existsByMarcaIgnoreCase(String marca);

    // Indica si existe algún vehículo registrado con esa categoría (para no dejar
    // eliminar una categoría del catálogo mientras siga en uso)
    boolean existsByCategoriaIgnoreCase(String categoria);
}
