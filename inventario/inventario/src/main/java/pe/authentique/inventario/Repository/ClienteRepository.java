package pe.authentique.inventario.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.authentique.inventario.model.Cliente;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    // Ejemplo de consulta personalizada para buscar clientes por nombre
    List<Cliente> findByNombreContaining(String nombre);

    // Puedes agregar más métodos personalizados según sea necesario
}

