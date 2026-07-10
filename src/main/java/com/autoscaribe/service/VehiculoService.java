package com.autoscaribe.service;

import com.autoscaribe.domain.Vehiculo;
import com.autoscaribe.repository.VehiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public List<Vehiculo> getVehiculos() {
        return vehiculoRepository.findAll();
    }
}