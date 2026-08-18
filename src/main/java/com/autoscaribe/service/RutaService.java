/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoscaribe.service;

import com.autoscaribe.domain.Ruta;
import com.autoscaribe.repository.RutaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Servicio que trae las rutas protegidas desde la BD
@Service
public class RutaService {

    private final RutaRepository rutaRepository;

    // Inyecta el repositorio de rutas
    public RutaService(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    // Trae todas las rutas ordenadas (primero las públicas)
    @Transactional(readOnly = true)
    public List<Ruta> getRutas() {
        return rutaRepository.findAllByOrderByRequiereRolAsc();
    }
}