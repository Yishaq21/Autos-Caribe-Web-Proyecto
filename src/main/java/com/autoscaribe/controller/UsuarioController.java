package com.autoscaribe.controller;

import com.autoscaribe.domain.Usuario;
import com.autoscaribe.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controlador para que el administrador gestione usuarios
@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final MessageSource messageSource;

    // Inyecta el servicio de usuarios y el de mensajes
    public UsuarioController(UsuarioService usuarioService, MessageSource messageSource) {
        this.usuarioService = usuarioService;
        this.messageSource = messageSource;
    }

    // Muestra el listado de usuarios activos
    @GetMapping("/listado")
    public String listado(Model model) {
        var usuarios = usuarioService.getUsuarios(false);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());
        return "/usuario/listado";
    }

    // Guarda un usuario nuevo o actualiza uno existente
    @PostMapping("/guardar")
    public String guardar(@Valid Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.save(usuario);
            redirectAttributes.addFlashAttribute("todoOk",
                    messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));
        } catch (DataIntegrityViolationException e) {
            // Error si el usuario o correo ya existe
            redirectAttributes.addFlashAttribute("error", "El nombre de usuario o correo ya existe en el sistema.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuario/listado";
    }

    // Desactiva un usuario (no lo borra de la BD)
    @PostMapping("/desactivar")
    public String desactivar(@RequestParam Integer idUsuario, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.desactivar(idUsuario);
            redirectAttributes.addFlashAttribute("todoOk",
                    messageSource.getMessage("usuario.desactivado", null, Locale.getDefault()));
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuario/listado";
    }

    // Carga el formulario para editar un usuario
    @GetMapping("/modificar/{idUsuario}")
    public String modificar(@PathVariable("idUsuario") Integer idUsuario, Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioService.getUsuario(idUsuario);

        // Si no existe el usuario, vuelve al listado
        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("usuario.error01", null, Locale.getDefault()));
            return "redirect:/usuario/listado";
        }

        // Limpia el password para no mostrar el hash en el formulario
        Usuario usuario = usuarioOpt.get();
        usuario.setPassword("");
        model.addAttribute("usuario", usuario);
        return "/usuario/modifica";
    }

    // Obtiene el usuario que está logueado actualmente
    private Usuario getUsuarioLogueado() {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        return usuarioService.getUsuarioPorUsername(auth.getName()).orElse(null);
    }

    // Muestra el perfil del usuario logueado
    @GetMapping("/perfil")
    public String perfil(Model model) {
        Usuario usuario = getUsuarioLogueado();
        if (usuario == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", usuario);
        return "/usuario/perfil";
    }

    // Actualiza los datos del perfil del usuario logueado
    @PostMapping("/perfil/actualizar")
    public String actualizarPerfil(@RequestParam String nombre,
            @RequestParam String apellidos,
            @RequestParam String correo,
            @RequestParam(required = false) String telefono,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = getUsuarioLogueado();
        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            usuarioService.actualizarPerfil(usuario.getIdUsuario(), nombre, apellidos, correo, telefono);
            redirectAttributes.addFlashAttribute("todoOk",
                    messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));
        } catch (DataIntegrityViolationException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuario/perfil";
    }
}
