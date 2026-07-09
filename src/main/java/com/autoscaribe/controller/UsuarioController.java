/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoscaribe.controller;

import com.autoscaribe.domain.Usuario;
import com.autoscaribe.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Isaac
 */

//HU-16: Gestionar usuarios CRUD 
@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final MessageSource messageSource;

    public UsuarioController(UsuarioService usuarioService, MessageSource messageSource) {
        this.usuarioService = usuarioService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado") //Carga el listado de todos los usuarios registrado
    public String listado(Model model) {
        var usuarios = usuarioService.getUsuarios(false);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());
        return "/usuario/listado";
    }

@PostMapping("/guardar") // Creacion y actualizacion deun usuario
    public String guardar(@Valid Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.save(usuario);
            redirectAttributes.addFlashAttribute("todoOk", messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Usamos un mensaje directo o puedes crear uno en tu messages.properties
            redirectAttributes.addFlashAttribute("error", "Error: El nombre de usuario ya existe en el sistema.");
        }
        return "redirect:/usuario/listado";
    }

    @PostMapping("/eliminar") //Elimina un usuario 
    public String eliminar(@RequestParam Integer idUsuario, RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";
        try {
            usuarioService.delete(idUsuario);
        } catch (IllegalArgumentException e) {
            titulo = "error"; 
            detalle = "usuario.error01"; // usuario no existe
        } catch (IllegalStateException e) {
            titulo = "error"; 
            detalle = "usuario.error02"; // tiene datos asociados
        } catch (Exception e) {
            titulo = "error";  
            detalle = "usuario.error03"; //error inesperado
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/usuario/listado";
    }

    @GetMapping("/modificar/{idUsuario}") // carga los datos del usuario
    public String modificar(@PathVariable("idUsuario") Integer idUsuario, Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioService.getUsuario(idUsuario);
        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("usuario.error01", null, Locale.getDefault()));
            return "redirect:/usuario/listado";
        }
        model.addAttribute("usuario", usuarioOpt.get());
        return "/usuario/modifica";
    }
}