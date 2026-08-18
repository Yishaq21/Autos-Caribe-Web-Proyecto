package com.autoscaribe.controller;

import com.autoscaribe.domain.Usuario;
import com.autoscaribe.service.RolService;
import com.autoscaribe.service.UsuarioService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario_rol")
public class UsuarioRolController {

    private final UsuarioService usuarioService;
    private final RolService rolService;

    // Inyecta los servicios de usuario y rol
    public UsuarioRolController(UsuarioService usuarioService, RolService rolService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
    }

    // Muestra la pantalla inicial con la lista de usuarios
    @GetMapping("/mantenimiento")
    public String mantenimiento(Model model) {
        model.addAttribute("usuarios", usuarioService.getUsuarios(true));
        model.addAttribute("rolesDisponibles", rolService.getRoles());
        return "usuario_rol/mantenimiento";
    }

    // Al elegir un usuario, carga sus roles marcados
    @PostMapping("/consultar")
    public String consultar(@RequestParam("idUsuario") Integer idUsuario, Model model) {
        Optional<Usuario> usuarioOpt = usuarioService.getUsuario(idUsuario);

        // Si el usuario no existe, vuelve al inicio
        if (usuarioOpt.isEmpty()) {
            return "redirect:/usuario_rol/mantenimiento";
        }

        model.addAttribute("usuarios", usuarioService.getUsuarios(true));
        model.addAttribute("rolesDisponibles", rolService.getRoles());
        model.addAttribute("usuarioSeleccionado", usuarioOpt.get());
        return "usuario_rol/mantenimiento";
    }

    // Guarda los roles que el administrador marcó
    @PostMapping("/guardar")
    public String guardar(
            @RequestParam("idUsuario") Integer idUsuario,
            @RequestParam(value = "idRoles", required = false) List<Integer> idRoles,
            RedirectAttributes redirectAttributes) {

        try {
            usuarioService.actualizarRoles(idUsuario, idRoles);
            redirectAttributes.addFlashAttribute("todoOk", "Los permisos del usuario se actualizaron correctamente.");
        } catch (IllegalArgumentException e) {
            // Si hay error (ej: sin roles), muestra el mensaje
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/usuario_rol/mantenimiento";
    }
}