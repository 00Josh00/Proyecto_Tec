package pe.authentique.inventario.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.authentique.inventario.model.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioRepository  extends JpaRepository<Usuario, Integer> {

    //Obtener el usuario por su email
    //SELECT * FROM Usuario WHERE email = ?
    //Optional : es decir pude manejar 0 registros o null
    Optional<Usuario> findByEmail(String email);

    //Retornar un flag si el email del usuario existe: TRUE si existe, FALSE si no existe
    //exists
    //usuarios WHERE EXISTS(email) RETURN valor;
    boolean existsByEmail(String email);

}
