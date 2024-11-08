package pe.authentique.inventario.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.authentique.inventario.model.Producto;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    //Objetivo: Buscar los productos por nombre, y paginar los registros
    //Containing -> LIKE '%%'
    //find -> SELECT * FROM Curso
    //findByNombreContaining -> SELECT * FROM producto WHERE titulo like '%titulo%';
    //Pageable: configuraciones: ordenamiento, cantidad de registros x ejemplo, etc.
    Page<Producto> findByNombreContaining(String nombre, Pageable pageable);

    //Objetivo: Listar los ultimos 8 productos ordenados por fecha de creacion, descendente
    //SELECT TOP 8 * FROM producto ORDER BY fecha_creacion DESC
    //find -> SELECT * FROM
    //Top -> Top 8
    //OrderBy
    //Desc
    List<Producto> findTop3ByOrderByFechaCreacionDesc();
}
