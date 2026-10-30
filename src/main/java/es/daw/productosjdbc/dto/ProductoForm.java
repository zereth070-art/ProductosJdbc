package es.daw.productosjdbc.dto;

import es.daw.productosjdbc.model.Fabricante;
import es.daw.productosjdbc.model.Producto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto del formulario de producto (lo que viaja entre la vista y el controlador).
 *
 * ¿Por qué no usamos Producto directamente?
 * El <select> solo envía el código del fabricante (un número), no un Fabricante completo.
 * Aquí lo recogemos como Integer y lo validamos; después lo convertimos a Producto.
 */
@Data
@NoArgsConstructor
public class ProductoForm {

    private Integer codigo;            // null = alta, con valor = edición

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que 0")
    private Double precio;

    @NotNull(message = "Selecciona un fabricante")
    private Integer codigoFabricante;

    /** Producto (modelo) → formulario (vista). Se usa al editar. */
    public static ProductoForm desde(Producto p) {
        ProductoForm form = new ProductoForm();
        form.setCodigo(p.getCodigo());
        form.setNombre(p.getNombre());
        form.setPrecio(p.getPrecio());
        form.setCodigoFabricante(p.getFabricante().getCodigo());
        return form;
    }

    /** Formulario (vista) → Producto (modelo). Del fabricante solo tenemos el código. */
    public Producto aProducto() {
        Fabricante fabricante = new Fabricante(codigoFabricante, null);
        return new Producto(codigo, nombre, precio, fabricante);
    }
}
