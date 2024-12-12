package pe.authentique.inventario.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.authentique.inventario.model.EntradaInventario;

import java.time.LocalDateTime;
import java.util.List;

public interface EntradaInventarioRepository extends JpaRepository<EntradaInventario, Integer> {

    // Metodo para buscar entradas de inventario por el nombre del producto
    Page<EntradaInventario> findByProductoNombreContaining(String nombre, Pageable pageable);

    List<EntradaInventario> findByFechaCreacion(LocalDateTime fechaCreacion);
}
