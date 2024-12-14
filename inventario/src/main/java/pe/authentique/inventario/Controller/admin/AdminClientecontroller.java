package pe.authentique.inventario.Controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.authentique.inventario.Repository.ClienteRepository;
import pe.authentique.inventario.model.Cliente;

import java.util.List;

@Controller
@RequestMapping("/admin/clientes")
public class AdminClientecontroller {

    @Autowired
    private ClienteRepository clienteRepository;

    // Listar todos los clientes
    @GetMapping("")
    public String listarClientes(@PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<Cliente> clientesPage = clienteRepository.findAll(pageable);
        model.addAttribute("clientes", clientesPage);
        return "admin/clientes/index";
    }

    // Mostrar formulario para registrar un nuevo cliente
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "admin/clientes/nuevo";
    }

    // Procesar el registro de un nuevo cliente
    @PostMapping("/nuevo")
    public String registrar(@Validated Cliente cliente,
                            BindingResult br,
                            Model model,
                            RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("cliente", cliente);
            return "admin/clientes/nuevo";
        }

        clienteRepository.save(cliente);
        ra.addFlashAttribute("msgExito", "El cliente se ha registrado con éxito!");
        return "redirect:/admin/clientes";
    }

    // Mostrar formulario para editar un cliente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + id));
        model.addAttribute("cliente", cliente);
        return "admin/clientes/editar";
    }

    // Procesar la actualización de un cliente
    @PostMapping("/editar/{id}")
    public String actualizar(@PathVariable Integer id,
                             @Validated Cliente cliente,
                             BindingResult br,
                             Model model,
                             RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("cliente", cliente);
            return "admin/clientes/editar";
        }

        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + id));

        clienteExistente.setNombre(cliente.getNombre());
        clienteExistente.setApellidos(cliente.getApellidos());
        clienteExistente.setEmail(cliente.getEmail());
        clienteExistente.setDireccion(cliente.getDireccion());
        clienteExistente.setTelefono(cliente.getTelefono());

        clienteRepository.save(clienteExistente);
        ra.addFlashAttribute("msgExito", "El cliente se ha actualizado con éxito!");
        return "redirect:/admin/clientes";
    }

    // Eliminar un cliente
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes ra) {
        clienteRepository.deleteById(id);
        ra.addFlashAttribute("msgExito", "El cliente se ha eliminado con éxito!");
        return "redirect:/admin/clientes";
    }
}
