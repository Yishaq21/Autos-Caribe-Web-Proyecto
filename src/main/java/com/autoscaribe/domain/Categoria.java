package com.autoscaribe.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Data;

// Entidad que representa una categoría de vehículo (ej. Sedán, SUV, Pickup)
@Data
@Entity
@Table(name = "categoria")
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    // Llave primaria autoincremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    // Nombre de la categoría (única y obligatoria)
    @NotBlank
    @Column(name = "categoria", unique = true, length = 50)
    private String categoria;
}
