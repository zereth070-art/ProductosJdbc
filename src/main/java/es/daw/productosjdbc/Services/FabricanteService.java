package es.daw.productosjdbc.Services;

import es.daw.productosjdbc.exception.FabricanteConProductosException;
import es.daw.productosjdbc.exception.RecursoNoEncontradoException;
import es.daw.productosjdbc.model.Fabricante;
import es.daw.productosjdbc.repositories.FabricanteRepository;
import es.daw.productosjdbc.repositories.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FabricanteService {

    private final FabricanteRepository fabricanteRepository;
    private final ProductoRepository productoRepository;

    public FabricanteService(FabricanteRepository fabricanteRepository,
                             ProductoRepository productoRepository) {
        this.fabricanteRepository = fabricanteRepository;
        this.productoRepository = productoRepository;
    }

    public List<Fabricante> listar() {
        return fabricanteRepository.findAll();
    }

    public Fabricante buscarPorCodigo(Integer codigo) {
        return fabricanteRepository.findById(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el fabricante con código " + codigo));
    }

    public Fabricante crear(Fabricante fabricante) {
        fabricante.setNombre(fabricante.getNombre().trim());
        fabricanteRepository.save(fabricante);
        return fabricante;
    }

    public Fabricante actualizar(Fabricante fabricante) {
        buscarPorCodigo(fabricante.getCodigo());
        fabricante.setNombre(fabricante.getNombre().trim());
        fabricanteRepository.update(fabricante);
        return fabricante;
    }

    public void borrar(Integer codigo) {
        buscarPorCodigo(codigo);

        // Regla de negocio: comprobada aquí, antes de llegar a la BD
        if (productoRepository.existsByFabricanteCodigo(codigo)) {
            throw new FabricanteConProductosException(codigo);
        }
        fabricanteRepository.delete(codigo);
    }
}
