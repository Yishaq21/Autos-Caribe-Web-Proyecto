package com.autoscaribe.controller;

import com.autoscaribe.domain.Marca;
import com.autoscaribe.service.MarcaService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controlador para que el administrador gestione las marcas del catálogo (HU-18)
@Controller
@RequestMapping("/marca")
public class MarcaController {

    private final MarcaService marcaService;
    private final MessageSource messageSource;

    // Inyecta el servicio de marcas y el de mensajes
    public MarcaController(MarcaService marcaService, MessageSource messageSource) {
        this.marcaService = marcaService;
        this.messageSource = messageSource;
    }

    // Muestra la lista de todas las marcas
    @GetMapping("/listado")
    public String listado(Model model) {
        var marcas = marcaService.getMarcas();
        model.addAttribute("marcas", marcas);
        model.addAttribute("totalMarcas", marcas.size());
        model.addAttribute("marcaForm", new Marca()); // Para el formulario de nueva marca
        return "marca/listado";
    }

    // Guarda una marca nueva o editada
    // Se usa "marcaForm" como nombre del atributo del modelo (en vez del nombre por defecto "marca")
    // porque coincide con el nombre de la propiedad "marca" dentro de la entidad Marca. Cuando ambos
    // nombres son iguales, Spring MVC interpreta el único parámetro "marca" del formulario como si
    // fuera el objeto Marca completo (intentando buscarlo por id) en lugar de asignarlo a la propiedad,
    // lo que provocaba el error "Failed to convert value of type 'String' to required type 'Marca'".
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("marcaForm") @Valid Marca marca, RedirectAttributes redirectAttributes) {
        try {
            marcaService.save(marca);
            redirectAttributes.addFlashAttribute("todoOk",
                    messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Esa marca ya existe en el catálogo");
        }
        return "redirect:/marca/listado";
    }

    // Elimina una marca por su id
    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idMarca, RedirectAttributes redirectAttributes) {
        try {
            marcaService.delete(idMarca);
            redirectAttributes.addFlashAttribute("todoOk",
                    messageSource.getMessage("mensaje.eliminado", null, Locale.getDefault()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/marca/listado";
    }

    // Muestra el formulario para modificar una marca
    @GetMapping("/modificar/{idMarca}")
    public String modificar(@PathVariable("idMarca") Integer idMarca, Model model,
            RedirectAttributes redirectAttributes) {
        Optional<Marca> marcaOpt = marcaService.getMarca(idMarca);

        // Si la marca no existe, vuelve al listado con error
        if (marcaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("error", null, Locale.getDefault()));
            return "redirect:/marca/listado";
        }

        model.addAttribute("marcaForm", marcaOpt.get());
        return "marca/modifica";
    }
}

