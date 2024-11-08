package pe.authentique.inventario.Controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.authentique.inventario.Repository.EntradaInventarioRepository;
import pe.authentique.inventario.Repository.ProductoRepository;
import pe.authentique.inventario.model.EntradaInventario;
import pe.authentique.inventario.model.Producto;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/admin/entradasInventario")
public class AdminEntradaInventarioController {

    @Autowired
    private EntradaInventarioRepository entradaInventarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Listar todas las entradas de inventario
    @GetMapping("")
    String index(Model model,
                 @PageableDefault(size = 10, sort = "fechaCreacion") Pageable pageable) {
        Page<EntradaInventario> entradas = entradaInventarioRepository.findAll(pageable);
        model.addAttribute("entradas", entradas);
        return "admin/entradasInventario/index";
    }

    // Mostrar formulario para registrar una nueva entrada de inventario
    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        List<Producto> productos = productoRepository.findAll();
        productos.sort(Comparator.comparing(Producto::getNombre));

        // Crear una nueva instancia de EntradaInventario con producto nulo
        EntradaInventario entradaInventario = new EntradaInventario();
        entradaInventario.setProducto(null); // Asegura que no haya un producto seleccionado por defecto

        model.addAttribute("productos", productos);
        model.addAttribute("entradaInventario", entradaInventario);
        return "admin/entradasInventario/nueva";
    }

    // Procesar el registro de una nueva entrada de inventario
    @PostMapping("/nueva")
    String registrar(@Validated EntradaInventario entradaInventario,
                     BindingResult br,
                     Model model,
                     RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("entradaInventario", entradaInventario);
            model.addAttribute("productos", productoRepository.findAll());
            return "admin/entradasInventario/nueva";
        }

        // Guardar la entrada de inventario
        entradaInventarioRepository.save(entradaInventario);

        // Mensaje de éxito
        ra.addFlashAttribute("msgExito", "La entrada de inventario se ha registrado con éxito!");

        // Redireccionar a la lista de entradas
        return "redirect:/admin/entradasInventario";
    }

    @PostMapping("/eliminar/{id}")
    String eliminar(@PathVariable Integer id, RedirectAttributes ra) {
        //1. eliminamos el curso por su ID, utilizando el repository
        entradaInventarioRepository.deleteById(id);

        //2. Creo un mensaje flash
        ra.addFlashAttribute("msgExito", "El producto se ha eliminado con éxito!");

        //3. redireccionamos a la página de inicio de productos
        return "redirect:/admin/entradasInventario";
    }
}
