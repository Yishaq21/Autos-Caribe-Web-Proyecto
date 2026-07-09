/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoscaribe.controller;


import com.autoscaribe.service.RolService;
import com.autoscaribe.service.UsuarioService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 *
 * @author Isaac
 */


// HU-17: Gestión de roles y permisos.

@Controller
@RequestMapping("/usuario_rol")
public class UsuarioRolController {

    private final UsuarioService usuarioService;
    private final RolService rolService;

    public UsuarioRolController(UsuarioService usuarioService, RolService rolService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
    }


//Carga la vista inicial del mantenimiento cargando la lista de usuarios

    @GetMapping("/mantenimiento")
    public String mantenimiento(Model model) {
        model.addAttribute("usuarios", usuarioService.getUsuarios(true));
        return "/usuario_rol/mantenimiento";
    }


 // seleciona un usuario y carga sus roles actuale

    @PostMapping("/consultar")
    public String consultar(@RequestParam Integer idUsuario, Model model) {
        var usuarioOpt = usuarioService.getUsuario(idUsuario);
        
        if (usuarioOpt.isPresent()) {
            model.addAttribute("usuarioSeleccionado", usuarioOpt.get());
            model.addAttribute("rolesDisponibles", rolService.getRoles());
        }
        
        model.addAttribute("usuarios", usuarioService.getUsuarios(true));
        return "/usuario_rol/mantenimiento";
    }


// Persiste los cambios de roles seleccionados en la BD

    @PostMapping("/guardar")
    public String guardar(@RequestParam Integer idUsuario, 
                          @RequestParam(required = false) List<Integer> idRoles, 
                          RedirectAttributes redirectAttributes) {
        
        var usuarioOpt = usuarioService.getUsuario(idUsuario);
        
        if (usuarioOpt.isPresent()) {
            var usuario = usuarioOpt.get();
            
         // limpia roles
            usuario.getRoles().clear();
            
            // Reasignamos los roles seleccionados
            if (idRoles != null) {
                for (Integer idRol : idRoles) {
                    rolService.getRol(idRol).ifPresent(rol -> usuario.getRoles().add(rol));
                }
            }
            
            usuarioService.save(usuario);
            redirectAttributes.addFlashAttribute("todoOk", "Permisos actualizados correctamente");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar los permisos");
        }
        
        return "redirect:/usuario_rol/mantenimiento";
    }
}