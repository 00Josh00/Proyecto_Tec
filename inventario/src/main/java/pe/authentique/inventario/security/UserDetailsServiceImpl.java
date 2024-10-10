package pe.authentique.inventario.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.authentique.inventario.Repository.UsuarioRepository;
import pe.authentique.inventario.model.Usuario;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario =  usuarioRepository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("El usuario no ha sido encontrado"));
        return new AppUserDetails(usuario);
    }
}