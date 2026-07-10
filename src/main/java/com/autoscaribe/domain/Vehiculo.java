package com.autoscaribe.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehiculo")
public class Vehiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private Integer idVehiculo;

    @NotEmpty
    @Column(name = "marca", length = 40)
    private String marca;

    @NotEmpty
    @Column(name = "modelo", length = 40)
    private String modelo;

    @Column(name = "año")
    private Integer año;

    @Column(name = "precio")
    private BigDecimal precio;

    @Column(name = "ruta_imagen", length = 255)
    private String rutaImagen;
}
