package com.autoscaribe.controller;

import com.autoscaribe.service.CatalogoService;
import java.math.BigDecimal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping({"/", "/catalogo"})
    public String catalogo(Model model) {
        model.addAttribute("vehiculos", catalogoService.listarTodos());
        prepararFiltros(model);
        return "catalogo/listado";
    }

    @GetMapping("/catalogo/buscar")
    public String buscarPorMarca(@RequestParam(required = false) String marca,
            Model model) {
        model.addAttribute("vehiculos", catalogoService.buscarPorMarca(marca));
        prepararFiltros(model);
        model.addAttribute("marca", marca == null ? "" : marca.trim());
        return "catalogo/listado";
    }

    @GetMapping("/catalogo/categoria")
    public String filtrarPorCategoria(
            @RequestParam(required = false) String categoria,
            Model model) {
        model.addAttribute("vehiculos",
                catalogoService.buscarPorCategoria(categoria));
        prepararFiltros(model);
        model.addAttribute("categoria",
                categoria == null ? "" : categoria.trim());
        return "catalogo/listado";
    }

    @GetMapping("/catalogo/precio")
    public String filtrarPorPrecio(
            @RequestParam(required = false) BigDecimal precioMinimo,
            @RequestParam(required = false) BigDecimal precioMaximo,
            Model model) {

        if (precioMinimo != null && precioMaximo != null
                && precioMinimo.compareTo(precioMaximo) > 0) {
            BigDecimal temporal = precioMinimo;
            precioMinimo = precioMaximo;
            precioMaximo = temporal;
        }

        model.addAttribute("vehiculos",
                catalogoService.buscarPorPrecio(precioMinimo, precioMaximo));
        prepararFiltros(model);
        model.addAttribute("precioMinimo", precioMinimo);
        model.addAttribute("precioMaximo", precioMaximo);
        return "catalogo/listado";
    }

    @GetMapping({"/catalogo/detalle/{idVehiculo}", "/catalogo/{idVehiculo}"})
    public String detalle(@PathVariable Integer idVehiculo,
            Model model, RedirectAttributes redirectAttributes) {

        var vehiculo = catalogoService.buscarPorId(idVehiculo);
        if (vehiculo.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vehículo no encontrado");
            return "redirect:/catalogo";
        }
        model.addAttribute("vehiculo", vehiculo.get());
        return "catalogo/detalle";
    }

    private void prepararFiltros(Model model) {
        if (!model.containsAttribute("marca")) {
            model.addAttribute("marca", "");
        }
        if (!model.containsAttribute("categoria")) {
            model.addAttribute("categoria", "");
        }
        if (!model.containsAttribute("precioMinimo")) {
            model.addAttribute("precioMinimo", "");
        }
        if (!model.containsAttribute("precioMaximo")) {
            model.addAttribute("precioMaximo", "");
        }
    }
}
