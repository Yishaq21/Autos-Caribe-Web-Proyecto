package com.autoscaribe.repository;

import com.autoscaribe.domain.ImagenVehiculo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagenVehiculoRepository extends JpaRepository<ImagenVehiculo, Integer> {

    List<ImagenVehiculo> findByVehiculo_IdVehiculoOrderByOrdenAsc(Integer idVehiculo);

    long countByVehiculo_IdVehiculo(Integer idVehiculo);

    void deleteByVehiculo_IdVehiculo(Integer idVehiculo);
}
