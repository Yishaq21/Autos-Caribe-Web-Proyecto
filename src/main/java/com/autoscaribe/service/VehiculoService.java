package com.autoscaribe.service;

import com.autoscaribe.domain.Vehiculo;
import com.autoscaribe.repository.VehiculoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> getVehiculos() {
        return vehiculoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Vehiculo> getVehiculo(Integer idVehiculo) {
        return vehiculoRepository.findById(idVehiculo);
    }

    @Transactional
    public void save(Vehiculo vehiculo) {
        vehiculoRepository.save(vehiculo);
    }

    @Transactional
    public void delete(Integer idVehiculo) {
        if (!vehiculoRepository.existsById(idVehiculo)) {
            throw new IllegalArgumentException("El vehículo no existe");
        }
        try {
            vehiculoRepository.deleteById(idVehiculo);
            vehiculoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el vehículo porque tiene solicitudes asociadas", e);
        }
    }
}
