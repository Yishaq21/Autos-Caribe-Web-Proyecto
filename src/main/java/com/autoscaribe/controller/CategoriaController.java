package com.autoscaribe.controller;

import com.autoscaribe.domain.Categoria;
import com.autoscaribe.service.CategoriaService;
import jakarta.validation.Valid;
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

// Controlador para que el administrador gestione las categorías del catálogo (HU-19)
@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final MessageSource messageSource;

    // Inyecta el servicio de categorías y el de mensajes
    public CategoriaController(CategoriaService categoriaService, MessageSource messageSource) {
        this.categoriaService = categoriaService;
        this.messageSource = messageSource;
    }

    // Muestra la lista de todas las categorías
    @GetMapping("/listado")
    public String listado(Model model) {
        var categorias = categoriaService.getCategorias();
        model.addAttribute("categorias", categorias);
        model.addAttribute("totalCategorias", categorias.size());
        model.addAttribute("categoria", new Categoria()); // Para el formulario de nueva categoría
        return "categoria/listado";
    }

    // Guarda una categoría nueva o editada
    @PostMapping("/guardar")
    public String guardar(@Valid Categoria categoria, RedirectAttributes redirectAttributes) {
        try {
            categoriaService.save(categoria);
            redirectAttributes.addFlashAttribute("todoOk",
                    messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Esa categoría ya existe en el catálogo");
        }
        return "redirect:/categoria/listado";
    }

    // Elimina una categoría por su id
    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idCategoria, RedirectAttributes redirectAttributes) {
        try {
            categoriaService.delete(idCategoria);
            redirectAttributes.addFlashAttribute("todoOk",
                    messageSource.getMessage("mensaje.eliminado", null, Locale.getDefault()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/categoria/listado";
    }

    // Muestra el formulario para modificar una categoría
    @GetMapping("/modificar/{idCategoria}")
    public String modificar(@PathVariable("idCategoria") Integer idCategoria, Model model,
            RedirectAttributes redirectAttributes) {
        Optional<Categoria> categoriaOpt = categoriaService.getCategoria(idCategoria);

        // Si la categoría no existe, vuelve al listado con error
        if (categoriaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("error", null, Locale.getDefault()));
            return "redirect:/categoria/listado";
        }

        model.addAttribute("categoria", categoriaOpt.get());
        return "categoria/modifica";
    }
}
