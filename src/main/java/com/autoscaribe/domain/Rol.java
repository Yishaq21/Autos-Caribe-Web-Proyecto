package com.autoscaribe.domain;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
/**
 *
 * @author Isaac
 */
@Data
@Entity
@Table(name = "rol")
public class Rol implements Serializable {
    
    // SerialVersionUID es buena práctica para versiones de persistencia
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