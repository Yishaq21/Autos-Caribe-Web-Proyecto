
package com.autoscaribe.controller;

import com.autoscaribe.domain.Rol;
import com.autoscaribe.domain.Usuario;
import com.autoscaribe.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 *
 * @author Isaac
 */

 //Controlador de Autenticación y Registro.
 //HU-01: Registro de usuario.
// HU-02: Inicio de sesión.

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


       //HU-02: Inicio de sesión

    @GetMapping("/login") // Panatalla de inicio de sesion
    public String login() {
        return "/auth/login"; 
    }

    @PostMapping("/login") // valida roles y procesa la contrase;a de usarios
    public String procesarLogin(@RequestParam String username, 
                                @RequestParam String password, 
                                HttpSession session, 
                                RedirectAttributes redirectAttributes) {
        
        var usuario = usuarioService.getUsuarioPorUsername(username); 
        
        if (usuario != null && usuario.getPassword().equals(password)) { // validacion manual 
            
            if (usuario.isActivo()) {
                session.setAttribute("usuarioLogueado", usuario);
                
                var esAdmin = false; 
                if (usuario.getRoles() != null) {
                    for (Rol rol : usuario.getRoles()) {
                        if (rol.getNombre().equalsIgnoreCase("Administrador") || 
                            rol.getNombre().equalsIgnoreCase("ADMIN") || 
                            rol.getNombre().equalsIgnoreCase("ROLE_ADMIN")) {
                            esAdmin = true;
                            break;
                        }
                    }
                }
                
                session.setAttribute("esAdmin", esAdmin);
                return "redirect:/"; 
            } else {
                redirectAttributes.addFlashAttribute("error", "El usuario está inactivo.");
                return "redirect:/login";
            }
            
        } else {
            redirectAttributes.addFlashAttribute("error", "Credenciales erróneas. Intenta de nuevo.");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout") //cerrar sesion
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }


       //HU-01: Registro de usuario

    @GetMapping("/registro") // formulario para registro de una nueva cuenta
    public String registro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "/auth/registro"; 
    }

    @PostMapping("/registro/guardar")
    public String guardarRegistro(Usuario usuario, RedirectAttributes redirectAttributes) {
        String titulo = "todoOk"; 
        String detalle = "¡Registro exitoso! Ya puedes iniciar sesión.";

        try {
            usuario.setActivo(true);
            usuarioService.save(usuario);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            titulo = "error";
            detalle = "Error: El nombre de usuario o correo ya está en uso.";
        }
        
        redirectAttributes.addFlashAttribute(titulo, detalle);
        
        if (titulo.equals("error")) {
            return "redirect:/registro";
        }
        return "redirect:/login";
    }
}