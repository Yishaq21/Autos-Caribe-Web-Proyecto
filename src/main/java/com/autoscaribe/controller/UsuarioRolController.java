package com.autoscaribe.controller;

import com.autoscaribe.domain.Rol;
import com.autoscaribe.domain.Usuario;
import com.autoscaribe.service.RolService;
import com.autoscaribe.service.UsuarioService;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/usuario_rol")
public class UsuarioRolController {

    private final UsuarioService usuarioService;
    private final RolService rolService;

    public UsuarioRolController(UsuarioService usuarioService, RolService rolService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
    }

    @GetMapping("/mantenimiento") //
    public String mantenimiento(Model model) {
        model.addAttribute("usuarios", usuarioService.getUsuarios(true));
        model.addAttribute("roles", rolService.getRoles());
        return "usuario_rol/mantenimiento"; // <-- CORRECCIÓN 2: Sin "/" al inicio
    }

    //  Recibir IDs 
    @PostMapping("/guardar")
    public String asignarRol(@RequestParam("idUsuario") Integer idUsuario, 
                             @RequestParam("idRol") Integer idRol) {
        
        Optional<Usuario> usuarioOpt = usuarioService.getUsuario(idUsuario);
        Optional<Rol> rolOpt = rolService.getRol(idRol);

        if (usuarioOpt.isPresent() && rolOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Rol rol = rolOpt.get();
            
            // Asignar el rol al usuario
            usuario.getRoles().add(rol);
            
            // Guardar el usuario 
            usuarioService.save(usuario); 
        }

        return "redirect:/usuario_rol/mantenimiento";
    }
}