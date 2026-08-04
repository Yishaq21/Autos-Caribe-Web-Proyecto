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
}
