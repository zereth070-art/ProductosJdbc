package es.daw.productosjdbc.repositories;

import es.daw.productosjdbc.model.Producto;

/**
 * Repositorio de productos: hereda el CRUD fijando T = Producto, ID = Integer
 * y añade las consultas propias de productos.
 */
public interface ProductoRepository extends GenericRepository<Producto, Integer> {

    /** ¿Hay algún producto de este fabricante? Lo usa FabricanteService antes de borrar. */
    boolean existsByFabricanteCodigo(Integer codigoFabricante);
}