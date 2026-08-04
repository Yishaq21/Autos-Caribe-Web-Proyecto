package com.autoscaribe.controller;

import com.autoscaribe.domain.Vehiculo;
import com.autoscaribe.service.VehiculoService;
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

@Controller
@RequestMapping("/vehiculo")
public class VehiculoController {

    private final VehiculoService vehiculoService;
    private final MessageSource messageSource;

    public VehiculoController(VehiculoService vehiculoService, MessageSource messageSource) {
        this.vehiculoService = vehiculoService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        model.addAttribute("vehiculos", vehiculoService.getVehiculos());
        return "/vehiculo/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        return "/vehiculo/modifica";
    }

    @PostMapping("/guardar")
    public String guardar(Vehiculo vehiculo, RedirectAttributes redirectAttributes) {
        vehiculoService.save(vehiculo);
        redirectAttributes.addFlashAttribute("todoOk", "Vehículo guardado correctamente");
        return "redirect:/vehiculo/listado";
    }

    @GetMapping("/modificar/{idVehiculo}")
    public String modificar(@PathVariable("idVehiculo") Integer idVehiculo, Model model, RedirectAttributes redirectAttributes) {
        Optional<Vehiculo> vehiculoOpt = vehiculoService.getVehiculo(idVehiculo);
        if (vehiculoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vehículo no encontrado");
            return "redirect:/vehiculo/listado";
        }
        model.addAttribute("vehiculo", vehiculoOpt.get());
        return "/vehiculo/modifica";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idVehiculo, RedirectAttributes redirectAttributes) {
        try {
            vehiculoService.delete(idVehiculo);
            redirectAttributes.addFlashAttribute("todoOk", "Vehículo eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el vehículo");
        }
        return "redirect:/vehiculo/listado";
    }
}
