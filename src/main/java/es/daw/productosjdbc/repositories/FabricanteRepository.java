package es.daw.productosjdbc.repositories;

import es.daw.productosjdbc.model.Fabricante;

/**
 * Repositorio de fabricantes: hereda el CRUD fijando T = Fabricante, ID = Integer.
 */
public interface FabricanteRepository extends GenericRepository<Fabricante, Integer> {

}
