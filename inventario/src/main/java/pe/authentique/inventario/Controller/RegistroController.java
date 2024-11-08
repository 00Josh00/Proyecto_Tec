package pe.authentique.inventario.Controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.authentique.inventario.Repository.UsuarioRepository;
import pe.authentique.inventario.model.Usuario;

@Controller
public class RegistroController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registro")
    public String index(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String crear(Model model, @Validated Usuario usuario, BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "registro";
        }

        //validaciones
        //Validar si el usuario existe en base de datos
        String email = usuario.getEmail();
        boolean usuarioExiste = usuarioRepository.existsByEmail(email);

        if (usuarioExiste) {
            br.rejectValue("email", "EmailAlredyExists");
        }

        //Validar las contraseñas 1 y 2 sean iguales
        if (!usuario.getPassword1().equals(usuario.getPassword2())) {
            br.rejectValue("password1", "PasswordNotEquals");
        }

        if (br.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "registro";
        }

        //Si no hay errores  de validacion, entonces registramos el usuario
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword1()));
        usuario.setRol(Usuario.Rol.ESTUDIANTE);
        usuarioRepository.save(usuario);

        ra.addFlashAttribute("registroExitoso", "Usuario registrado correctamente");
        return "redirect:/login";
    }
}