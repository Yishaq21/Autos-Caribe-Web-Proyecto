/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoscaribe.domain;

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
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @NotEmpty 
    @Column(name = "username", unique = true, nullable = false, length = 30)
    @Size(max = 30)
    private String username;

    @NotEmpty
    @Column(name = "password", nullable = false, length = 512)
    @Size(max = 512)
    private String password;

    @NotEmpty
    @Column(name = "nombre", nullable = false, length = 20)
    @Size(max = 20)
    private String nombre;

    @NotEmpty
    @Column(name = "apellidos", nullable = false, length = 30)
    @Size(max = 30)
    private String apellidos;

    @NotEmpty
    @Column(name = "correo", nullable = false, length = 75)
    @Size(max = 75)
    private String correo;

    @Column(name = "telefono", length = 25)
    @Size(max = 25)
    private String telefono;

    @Column(name = "ruta_imagen", length = 1024)
    @Size(max = 1024)
    private String rutaImagen;

    @Column(name = "activo")
    private boolean activo;

    @ManyToMany
    @JoinTable(
            name = "usuario_rol",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private List<Rol> roles;
}