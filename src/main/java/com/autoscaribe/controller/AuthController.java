
package com.autoscaribe.controller;

import com.autoscaribe.domain.Usuario;
import com.autoscaribe.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    // Muestra el formulario de registro
    @GetMapping("/registro")
    public String nuevo(Model model) {
        model.addAttribute("usuarioForm", new Usuario());
        return "auth/registro";
    }

    // Procesa el formulario de registro: crea el usuario con rol CLIENTE
    @PostMapping("/registro/guardar")
    public String guardar(@Valid @ModelAttribute("usuarioForm") Usuario usuario,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/registro";
        }
        try {
            usuarioService.registrar(usuario);
            redirectAttributes.addFlashAttribute("todoOk", "Su cuenta fue creada exitosamente. Ya puede iniciar sesión.");
            return "redirect:/login";
        } catch (DataIntegrityViolationException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/registro";
        }
    }
}