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

/**
 * Este servicio se encarga de traer la lista de rutas protegidas
 * desde la base de datos. El SecurityConfig usa esta lista para saber
 * qué rol necesita cada URL del sistema
 */
@Service
public class RutaService {

    private final RutaRepository rutaRepository;

    public RutaService(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    // Trae todas las rutas ordenadas: primero las públicas (requiereRol = false)
    // luego las que sí necesitan un rol específico
    @Transactional(readOnly = true)
    public List<Ruta> getRutas() {
        return rutaRepository.findAllByOrderByRequiereRolAsc();
    }
}