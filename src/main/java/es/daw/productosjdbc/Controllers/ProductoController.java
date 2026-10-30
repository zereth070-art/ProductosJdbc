package es.daw.productosjdbc.Controllers;

import es.daw.productosjdbc.Services.FabricanteService;
import es.daw.productosjdbc.Services.ProductoService;
import es.daw.productosjdbc.dto.ProductoForm;
import es.daw.productosjdbc.model.Producto;
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
@RequestMapping("/productos")
public class ProductoController {

    // El controlador solo conoce SERVICIOS, nunca repositorios
    private final ProductoService productoService;
    private final FabricanteService fabricanteService;

    public ProductoController(ProductoService productoService,
                              FabricanteService fabricanteService) {
        this.productoService = productoService;
        this.fabricanteService = fabricanteService;
    }

    // READ: listado
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoService.listar());
        return "productos/lista";
    }

    // CREATE: formulario vacío
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("productoForm", new ProductoForm());
        model.addAttribute("fabricantes", fabricanteService.listar());
        return "productos/formulario";
    }

    // UPDATE: formulario con los datos del producto
    @GetMapping("/editar/{codigo}")
    public String editar(@PathVariable Integer codigo, Model model) {
        model.addAttribute("productoForm",
                ProductoForm.desde(productoService.buscarPorCodigo(codigo)));
        model.addAttribute("fabricantes", fabricanteService.listar());
        return "productos/formulario";
    }

    // CREATE y UPDATE comparten formulario: el código oculto decide
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("productoForm") ProductoForm productoForm,
                          BindingResult resultado,
                          Model model,
                          RedirectAttributes flash) {
        if (resultado.hasErrors()) {
            // Hay que volver a cargar los fabricantes para pintar el <select>
            model.addAttribute("fabricantes", fabricanteService.listar());
            return "productos/formulario";
        }

        Producto producto = productoForm.aProducto();
        if (productoForm.getCodigo() == null) {
            productoService.crear(producto);
            flash.addFlashAttribute("mensaje", "Producto creado correctamente");
        } else {
            productoService.actualizar(producto);
            flash.addFlashAttribute("mensaje", "Producto actualizado correctamente");
        }
        return "redirect:/productos";
    }

    // DELETE
    @PostMapping("/borrar/{codigo}")
    public String borrar(@PathVariable Integer codigo, RedirectAttributes flash) {
        productoService.borrar(codigo);
        flash.addFlashAttribute("mensaje", "Producto borrado correctamente");
        return "redirect:/productos";
    }
}
