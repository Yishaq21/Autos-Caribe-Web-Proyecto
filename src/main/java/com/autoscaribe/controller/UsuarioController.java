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

/**
 * Controlador para que el ADMINISTRADOR gestione usuarios 
 */
@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final MessageSource messageSource;

    public UsuarioController(UsuarioService usuarioService, MessageSource messageSource) {
        this.usuarioService = usuarioService;
        this.messageSource = messageSource;
    }

    // Muestra el listado de usuarios (solo los activos aparecen aquí)
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
            redirectAttributes.addFlashAttribute("error", "El nombre de usuario o correo ya existe en el sistema.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuario/listado";
    }

    // Elimina un usuario (si no tiene datos asociados)
    @PostMapping("/eliminar")
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
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/usuario/listado";
    }

    // Carga el formulario de edición con los datos del usuario seleccionado
    @GetMapping("/modificar/{idUsuario}")
    public String modificar(@PathVariable("idUsuario") Integer idUsuario, Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioService.getUsuario(idUsuario);
        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("usuario.error01", null, Locale.getDefault()));
            return "redirect:/usuario/listado";
        }
        // Se limpia el password para que el formulario no muestre el hash
        Usuario usuario = usuarioOpt.get();
        usuario.setPassword("");
        model.addAttribute("usuario", usuario);
        return "/usuario/modifica";
    }
}