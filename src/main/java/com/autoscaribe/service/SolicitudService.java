package com.autoscaribe.service;

import com.autoscaribe.domain.Solicitud;
import com.autoscaribe.repository.SolicitudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;

    public SolicitudService(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    public List<Solicitud> getSolicitudes() {
        return solicitudRepository.findAll();
    }

    public List<Solicitud> getSolicitudesPorCliente(Integer idCliente) {
        return solicitudRepository.findByCliente_IdUsuario(idCliente);
    }

    public List<Solicitud> getSolicitudesPorVendedor(Integer idVendedor) {
        return solicitudRepository.findByVendedor_IdUsuario(idVendedor);
    }

    public Optional<Solicitud> getSolicitud(Integer idSolicitud) {
        return solicitudRepository.findById(idSolicitud);
    }

    public void save(Solicitud solicitud) {
        if (solicitud.getIdSolicitud() == null) {
            solicitud.setEstado("PENDIENTE");
            solicitud.setFechaSolicitud(LocalDateTime.now());
        }
        solicitudRepository.save(solicitud);
    }

    public void delete(Integer idSolicitud) {
        solicitudRepository.deleteById(idSolicitud);
    }
}