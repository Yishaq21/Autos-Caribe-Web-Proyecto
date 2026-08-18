package com.autoscaribe.repository;


import com.autoscaribe.domain.Ruta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Integer> {
// Trae todas las rutas ordenadas (primero las que no requieren rol)
    public List<Ruta> findAllByOrderByRequiereRolAsc();
}