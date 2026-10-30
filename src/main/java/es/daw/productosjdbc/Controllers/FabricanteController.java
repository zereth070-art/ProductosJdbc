package es.daw.productosjdbc.Controllers;

import es.daw.productosjdbc.Services.FabricanteService;
import es.daw.productosjdbc.exception.FabricanteConProductosException;
import es.daw.productosjdbc.model.Fabricante;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/fabricantes")
public class FabricanteController {

    private final FabricanteService fabricanteService;

    public FabricanteController(FabricanteService fabricanteService) {
        this.fabricanteService = fabricanteService;
    }

    // READ: listado
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("fabricantes", fabricanteService.listar());
        return "fabricantes/lista";
    }

    // CREATE: formulario vacío
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("fabricante", new Fabricante());
        return "fabricantes/formulario";
    }

    // UPDATE: formulario con los datos del fabricante
    @GetMapping("/editar/{codigo}")
    public String editar(@PathVariable Integer codigo, Model model) {
        model.addAttribute("fabricante", fabricanteService.buscarPorCodigo(codigo));
        return "fabricantes/formulario";
    }

    // CREATE y UPDATE comparten formulario: el código oculto decide
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("fabricante") Fabricante fabricante,
                          BindingResult resultado,
                          RedirectAttributes flash) {
        if (resultado.hasErrors()) {
            return "fabricantes/formulario";
        }

        if (fabricante.getCodigo() == null) {
            fabricanteService.crear(fabricante);
            flash.addFlashAttribute("mensaje", "Fabricante creado correctamente");
        } else {
            fabricanteService.actualizar(fabricante);
            flash.addFlashAttribute("mensaje", "Fabricante actualizado correctamente");
        }
        return "redirect:/fabricantes";
    }

    // DELETE: la regla de negocio vive en el servicio
    @PostMapping("/borrar/{codigo}")
    public String borrar(@PathVariable Integer codigo, RedirectAttributes flash) {
        try {
            fabricanteService.borrar(codigo);
            flash.addFlashAttribute("mensaje", "Fabricante borrado correctamente");
        } catch (FabricanteConProductosException e) {
            // Se muestra en el listado sin romper el flujo
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/fabricantes";
    }
}
