package es.daw.productosjdbc.repositories;

import es.daw.productosjdbc.exception.AccesoDatosException;
import es.daw.productosjdbc.model.Fabricante;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de FabricanteRepository.
 */
@Repository
public class FabricanteRepositoryJdbc implements FabricanteRepository {

    private final DataSource dataSource;

    public FabricanteRepositoryJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // ---------------------------------------------------------------- READ (uno)
    @Override
    public Optional<Fabricante> findById(Integer codigo) {
        String sql = "SELECT codigo, nombre FROM fabricante WHERE codigo = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar el fabricante " + codigo, e);
        }
    }

    // ---------------------------------------------------------------- READ (todos)
    @Override
    public List<Fabricante> findAll() {
        String sql = "SELECT codigo, nombre FROM fabricante ORDER BY nombre";
        List<Fabricante> fabricantes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                fabricantes.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar los fabricantes", e);
        }
        return fabricantes;
    }

    // ---------------------------------------------------------------- CREATE
    @Override
    public void save(Fabricante fabricante) {
        String sql = "INSERT INTO fabricante (nombre) VALUES (?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, fabricante.getNombre());
            ps.executeUpdate();

            // Recuperamos el código generado por AUTO_INCREMENT
            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    fabricante.setCodigo(claves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar el fabricante", e);
        }
    }

    // ---------------------------------------------------------------- UPDATE
    @Override
    public void update(Fabricante fabricante) {
        String sql = "UPDATE fabricante SET nombre = ? WHERE codigo = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, fabricante.getNombre());
            ps.setInt(2, fabricante.getCodigo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException(
                    "Error al actualizar el fabricante " + fabricante.getCodigo(), e);
        }
    }

    // ---------------------------------------------------------------- DELETE
    @Override
    public void delete(Integer codigo) {
        String sql = "DELETE FROM fabricante WHERE codigo = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al borrar el fabricante " + codigo, e);
        }
    }

    // ---------------------------------------------------------------- Mapeo fila → objeto
    private Fabricante mapRow(ResultSet rs) throws SQLException {
        return new Fabricante(
                rs.getInt("codigo"),
                rs.getString("nombre"));
    }
}
