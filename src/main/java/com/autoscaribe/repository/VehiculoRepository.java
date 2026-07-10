package com.autoscaribe.repository;

import com.autoscaribe.domain.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {
}