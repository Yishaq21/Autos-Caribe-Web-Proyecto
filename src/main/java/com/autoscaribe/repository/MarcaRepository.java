package com.autoscaribe.repository;

import com.autoscaribe.domain.Marca;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {

    // Busca una marca por su nombre (ejemplo: "Toyota")
    Optional<Marca> findByMarca(String marca);

    // Trae todas las marcas ordenadas alfabéticamente
    List<Marca> findAllByOrderByMarcaAsc();
}
