package pe.authentique.inventario.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.authentique.inventario.model.Producto;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    //Objetivo: Buscar los curso por nombre, y paginar los registros
    //Containing -> LIKE '%%'
    //find -> SELECT * FROM Curso
    //findByNombreContaining -> SELECT * FROM cursos WHERE titulo like '%titulo%';
    //Pageable: configuraciones: ordenamiento, cantidad de registros x ejemplo, etc.
    Page<Producto> findByNombreContaining(String nombre, Pageable pageable);

    //Objetivo: Listar los ultimos 8 cursos ordenados por fecha de creacion, descendente
    //SELECT TOP 8 * FROM curso ORDER BY fecha_creacion DESC
    //find -> SELECT * FROM
    //Top -> Top 8
    //OrderBy
    //Desc
    List<Producto> findTop8ByOrderByFechaCreacionDesc();
}
