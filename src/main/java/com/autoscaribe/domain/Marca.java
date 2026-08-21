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

// Entidad que representa una marca de vehículo (ej. Toyota, Honda, Hyundai)
@Data
@Entity
@Table(name = "marca")
public class Marca implements Serializable {

    private static final long serialVersionUID = 1L;

    // Llave primaria autoincremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private Integer idMarca;

    // Nombre de la marca (único y obligatorio)
    @NotBlank
    @Column(name = "marca", unique = true, length = 40)
    private String marca;
}
