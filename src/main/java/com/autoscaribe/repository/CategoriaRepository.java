package com.autoscaribe.repository;

import com.autoscaribe.domain.Categoria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    // Busca una categoría por su nombre (ejemplo: "SUV")
    Optional<Categoria> findByCategoria(String categoria);

    // Trae todas las categorías ordenadas alfabéticamente
    List<Categoria> findAllByOrderByCategoriaAsc();
}
