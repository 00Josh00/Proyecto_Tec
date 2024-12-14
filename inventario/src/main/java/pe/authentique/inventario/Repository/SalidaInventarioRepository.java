package pe.authentique.inventario.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.authentique.inventario.model.SalidaInventario;

import java.time.LocalDateTime;
import java.util.List;

public interface SalidaInventarioRepository extends JpaRepository<SalidaInventario, Long> {

    // Ejemplo de consulta personalizada: buscar salidas entre dos fechas
    List<SalidaInventario> findByFechaSalidaBetween(LocalDateTime startDate, LocalDateTime endDate);

    Page<SalidaInventario> findByClienteNombreContaining(String clienteNombre, Pageable pageable);

    // Puedes agregar más métodos personalizados según sea necesario
    @Query("SELECT s FROM SalidaInventario s LEFT JOIN FETCH s.detalles WHERE s.id = :id")
    SalidaInventario findByIdWithDetalles(@Param("id") Long id);
}

