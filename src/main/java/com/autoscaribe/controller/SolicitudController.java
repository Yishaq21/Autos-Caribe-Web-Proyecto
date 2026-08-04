package com.autoscaribe.controller;

import com.autoscaribe.domain.Solicitud;
import com.autoscaribe.domain.Usuario;
import com.autoscaribe.service.SolicitudService;
import com.autoscaribe.service.UsuarioService;
import com.autoscaribe.service.VehiculoService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/solicitud")
public class SolicitudController {

    private final SolicitudService solicitudService;
    private final VehiculoService vehiculoService;
    private final UsuarioService usuarioService;
    private final MessageSource messageSource;

    public SolicitudController(SolicitudService solicitudService,
                                VehiculoService vehiculoService,
                                UsuarioService usuarioService,
                                MessageSource messageSource) {
        this.solicitudService = solicitudService;
        this.vehiculoService = vehiculoService;
        this.usuarioService = usuarioService;
        this.messageSource = messageSource;
    }

    // Reemplaza el viejo session.getAttribute("usuarioLogueado").
    // Ahora se le pregunta directamente a Spring Security quién inició sesión.
    private Usuario getUsuarioLogueado() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        return usuarioService.getUsuarioPorUsername(auth.getName()).orElse(null);
    }

    private boolean tieneRol(Usuario usuario, String nombreRol) {
        if (usuario == null || usuario.getRoles() == null) {
            return false;
        }
        return usuario.getRoles().stream()
                .anyMatch(r -> r.getRol().equalsIgnoreCase(nombreRol));
    }

    @GetMapping("/listado")
    public String listado(Model model, @RequestParam(required = false) Integer idVehiculo) {
        Usuario usuarioLogueado = getUsuarioLogueado();

        List<Solicitud> solicitudes;
        if (usuarioLogueado != null && tieneRol(usuarioLogueado, "VENDEDOR")) {
            solicitudes = solicitudService.getSolicitudesPorVendedor(usuarioLogueado.getIdUsuario());
        } else if (usuarioLogueado != null && tieneRol(usuarioLogueado, "CLIENTE")) {
            solicitudes = solicitudService.getSolicitudesPorCliente(usuarioLogueado.getIdUsuario());
        } else {
            solicitudes = solicitudService.getSolicitudes();
        }

        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("solicitud", new Solicitud());
        model.addAttribute("idVehiculoSeleccionado", idVehiculo);
        model.addAttribute("totalSolicitudes", solicitudes.size());
        model.addAttribute("vehiculos", vehiculoService.getVehiculos());
        model.addAttribute("vendedores", usuarioService.getUsuarios(true).stream()
                .filter(u -> tieneRol(u, "VENDEDOR"))
                .toList());
        return "/solicitud/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Solicitud solicitud, RedirectAttributes redirectAttributes) {
        try {
            if (solicitud.getIdSolicitud() == null) {
                solicitud.setCliente(getUsuarioLogueado());
            }
            solicitudService.save(solicitud);
            redirectAttributes.addFlashAttribute("todoOk",
                    messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error: No se pudo guardar la solicitud. Verifique los datos.");
        }
        return "redirect:/solicitud/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(Integer idSolicitud, RedirectAttributes redirectAttributes) {
        String tipoMensaje = "todoOk";
        String mensajeKey = "mensaje.eliminado";
        try {
            solicitudService.delete(idSolicitud);
        } catch (IllegalArgumentException e) {
            tipoMensaje = "error";
            mensajeKey = "solicitud.error01";
        } catch (IllegalStateException e) {
            tipoMensaje = "error";
            mensajeKey = "solicitud.error02";
        } catch (Exception e) {
            tipoMensaje = "error";
            mensajeKey = "solicitud.error03";
        }
        redirectAttributes.addFlashAttribute(tipoMensaje,
                messageSource.getMessage(mensajeKey, null, Locale.getDefault()));
        return "redirect:/solicitud/listado";
    }

    @GetMapping("/modificar/{idSolicitud}")
    public String modificar(@PathVariable Integer idSolicitud, Model model, RedirectAttributes redirectAttributes) {
        var solicitudOpt = solicitudService.getSolicitud(idSolicitud);
        if (solicitudOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("solicitud.error01", null, Locale.getDefault()));
            return "redirect:/solicitud/listado";
        }
        model.addAttribute("solicitud", solicitudOpt.get());
        return "/solicitud/modifica";
    }
}