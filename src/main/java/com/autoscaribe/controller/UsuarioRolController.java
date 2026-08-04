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


@Controller
@RequestMapping("/usuario_rol")
public class UsuarioRolController {

    private final UsuarioService usuarioService;
    private final RolService rolService;

    public UsuarioRolController(UsuarioService usuarioService, RolService rolService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
    }

    // Pantalla principal: lista de usuarios activos para elegir a cuál editarle los roles
    @GetMapping("/mantenimiento")
    public String mantenimiento(Model model) {
        model.addAttribute("usuarios", usuarioService.getUsuarios(true));
        return "/usuario_rol/mantenimiento";
    }

    // Al seleccionar un usuario, muestra sus roles actuales y todos los roles disponibles
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

    // Reemplaza los roles del usuario por los que se marcaron en el formulario
    @PostMapping("/guardar")
    public String guardar(@RequestParam Integer idUsuario,
                          @RequestParam(required = false) List<Integer> idRoles,
                          RedirectAttributes redirectAttributes) {

        var usuarioOpt = usuarioService.getUsuario(idUsuario);

        if (usuarioOpt.isPresent()) {
            var usuario = usuarioOpt.get();

            usuario.getRoles().clear();

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
