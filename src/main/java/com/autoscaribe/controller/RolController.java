/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoscaribe.controller;

import com.autoscaribe.domain.Rol;
import com.autoscaribe.service.RolService;
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

// HU-17: Gestionar roles. para la gestión de Roles.
@Controller
@RequestMapping("/rol")
public class RolController {

    private final RolService rolService;
    private final MessageSource messageSource;

    public RolController(RolService rolService, MessageSource messageSource) {
        this.rolService = rolService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var roles = rolService.getRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("totalRoles", roles.size());
        model.addAttribute("rol", new Rol());
        return "/rol/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Rol rol, RedirectAttributes redirectAttributes) {
        rolService.save(rol);
        redirectAttributes.addFlashAttribute("todoOk", messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));
        return "redirect:/rol/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idRol, RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";
        try {
            rolService.delete(idRol);
        } catch (IllegalArgumentException e) {
            titulo = "error"; 
            detalle = "error"; // uso error generico 
        } catch (IllegalStateException e) {
            titulo = "error"; 
            detalle = "error";
        } catch (Exception e) {
            titulo = "error";  
            detalle = "error";
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/rol/listado";
    }

    @GetMapping("/modificar/{idRol}")
    public String modificar(@PathVariable("idRol") Integer idRol, Model model, RedirectAttributes redirectAttributes) {
        Optional<Rol> rolOpt = rolService.getRol(idRol);
        if (rolOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("error", null, Locale.getDefault()));
            return "redirect:/rol/listado";
        }
        model.addAttribute("rol", rolOpt.get());
        return "/rol/modifica";
    }
}