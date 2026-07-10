package com.autoscaribe.repository;

import com.autoscaribe.domain.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {

    List<Solicitud> findByCliente_IdUsuario(Integer idCliente);

    List<Solicitud> findByVendedor_IdUsuario(Integer idVendedor);
}