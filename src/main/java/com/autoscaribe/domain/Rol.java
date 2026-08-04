package com.autoscaribe.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
@Data
@Entity
@Table(name = "rol")
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;

    @NotEmpty // no permite strings vacios
    @Size(max = 20)
    @Column(name = "rol", unique = true, nullable = false, length = 20)
    private String nombre;

    @ManyToMany(mappedBy = "roles")
    private List<Usuario> usuarios;
}
