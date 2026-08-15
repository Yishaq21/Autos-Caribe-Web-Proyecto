package com.autoscaribe.controller;

import com.autoscaribe.domain.Vehiculo;
import com.autoscaribe.service.ImagenVehiculoService;
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
    private final ImagenVehiculoService imagenVehiculoService;
    private final MessageSource messageSource;

    public VehiculoController(VehiculoService vehiculoService,
            ImagenVehiculoService imagenVehiculoService,
            MessageSource messageSource) {
        this.vehiculoService = vehiculoService;
        this.imagenVehiculoService = imagenVehiculoService;
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
        model.addAttribute("imagenes", java.util.Collections.emptyList());
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
        model.addAttribute("imagenes", imagenVehiculoService.getImagenes(idVehiculo));
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

    @PostMapping("/{idVehiculo}/imagenes/agregar")
    public String agregarImagen(@PathVariable("idVehiculo") Integer idVehiculo,
            @RequestParam String rutaImagen,
            RedirectAttributes redirectAttributes) {

        Optional<Vehiculo> vehiculoOpt = vehiculoService.getVehiculo(idVehiculo);
        if (vehiculoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vehículo no encontrado");
            return "redirect:/vehiculo/listado";
        }
        if (rutaImagen == null || rutaImagen.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Debe indicar la dirección de la imagen");
            return "redirect:/vehiculo/modificar/" + idVehiculo;
        }

        imagenVehiculoService.agregar(vehiculoOpt.get(), rutaImagen);
        redirectAttributes.addFlashAttribute("todoOk", "Imagen agregada correctamente");
        return "redirect:/vehiculo/modificar/" + idVehiculo;
    }

    @PostMapping("/imagenes/eliminar")
    public String eliminarImagen(@RequestParam Integer idImagen,
            @RequestParam Integer idVehiculo,
            RedirectAttributes redirectAttributes) {
        try {
            imagenVehiculoService.eliminar(idImagen);
            redirectAttributes.addFlashAttribute("todoOk", "Imagen eliminada correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la imagen");
        }
        return "redirect:/vehiculo/modificar/" + idVehiculo;
    }
}
