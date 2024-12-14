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
import pe.authentique.inventario.Repository.ClienteRepository;
import pe.authentique.inventario.Repository.ProductoRepository;
import pe.authentique.inventario.Repository.SalidaInventarioRepository;
import pe.authentique.inventario.model.DetalleSalida;
import pe.authentique.inventario.model.Producto;
import pe.authentique.inventario.model.SalidaInventario;

import java.util.List;

@Controller
@RequestMapping("/admin/salidasInventario")
public class AdminSalidaInventarioController {

    @Autowired
    private SalidaInventarioRepository salidaInventarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/nueva")
    public String nuevaSalida(Model model) {
        model.addAttribute("salidaInventario", new SalidaInventario());
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("clientes", clienteRepository.findAll());
        return "admin/salidasInventario/nueva";
    }

    @PostMapping("/nueva")
    public String registrarSalida(@Validated @ModelAttribute SalidaInventario salidaInventario,
                                  BindingResult br,
                                  RedirectAttributes ra) {
        // Validar errores del formulario global
        if (br.hasErrors()) {
            return "admin/salidasInventario/nueva";
        }

        // Validar que haya al menos un detalle válido
        salidaInventario.getDetalles().removeIf(detalle -> detalle.getProducto() == null || detalle.getCantidad() == null || detalle.getCantidad() <= 0);

        if (salidaInventario.getDetalles().isEmpty()) {
            br.rejectValue("detalles", "NoSeleccionados", "Debe seleccionar al menos un producto con cantidad válida.");
            return "admin/salidasInventario/nueva";
        }

        // Procesar cada detalle de la salida
        for (DetalleSalida detalle : salidaInventario.getDetalles()) {
            Producto producto = productoRepository.findById(detalle.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // Validar stock disponible
            if (producto.getStock() < detalle.getCantidad()) {
                br.rejectValue("detalles", "StockInsuficiente",
                        "Stock insuficiente para el producto: " + producto.getNombre());
                return "admin/salidasInventario/nueva";
            }

            // Reducir el stock del producto
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);

            // Vincular el detalle a la salida
            detalle.setSalidaInventario(salidaInventario);
        }

        // Guardar la salida con sus detalles
        salidaInventarioRepository.save(salidaInventario);

        // Mensaje de éxito
        ra.addFlashAttribute("msgExito", "Salida registrada con éxito.");
        return "redirect:/admin/salidasInventario";
    }

    @GetMapping("")
    public String listarSalidas(Model model,
                                @PageableDefault(size = 10, sort = "fechaSalida") Pageable pageable,
                                @RequestParam(required = false) String clienteNombre) {
        Page<SalidaInventario> salidas;

        if (clienteNombre != null && !clienteNombre.trim().isEmpty()) {
            salidas = salidaInventarioRepository.findByClienteNombreContaining(clienteNombre, pageable);
        } else {
            salidas = salidaInventarioRepository.findAll(pageable);
        }

        model.addAttribute("salidas", salidas);
        return "admin/salidasInventario/index";
    }

    @GetMapping("/editar/{id}")
    public String editarSalida(@PathVariable Long id, Model model) {
        SalidaInventario salidaInventario = salidaInventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salida de inventario no encontrada"));

        model.addAttribute("salidaInventario", salidaInventario);
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("clientes", clienteRepository.findAll());
        return "admin/salidasInventario/editar";
    }

    @PostMapping("/editar/{id}")
    public String actualizarSalida(@PathVariable Long id,
                                   @Validated @ModelAttribute SalidaInventario salidaInventario,
                                   BindingResult br,
                                   RedirectAttributes ra) {
        // Validar errores del formulario global
        if (br.hasErrors()) {
            return "admin/salidasInventario/editar";
        }

        // Validar que haya al menos un detalle válido
        salidaInventario.getDetalles().removeIf(detalle -> detalle.getProducto() == null || detalle.getCantidad() == null || detalle.getCantidad() <= 0);

        if (salidaInventario.getDetalles().isEmpty()) {
            br.rejectValue("detalles", "NoSeleccionados", "Debe seleccionar al menos un producto con cantidad válida.");
            return "admin/salidasInventario/editar";
        }

        // Procesar cada detalle de la salida
        for (DetalleSalida detalle : salidaInventario.getDetalles()) {
            Producto producto = productoRepository.findById(detalle.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // Validar stock disponible
            if (producto.getStock() < detalle.getCantidad()) {
                br.rejectValue("detalles", "StockInsuficiente",
                        "Stock insuficiente para el producto: " + producto.getNombre());
                return "admin/salidasInventario/editar";
            }

            // Reducir el stock del producto
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);

            // Vincular el detalle a la salida
            detalle.setSalidaInventario(salidaInventario);
        }

        // Actualizar la salida con sus detalles
        salidaInventario.setId(id);
        salidaInventarioRepository.save(salidaInventario);

        // Mensaje de éxito
        ra.addFlashAttribute("msgExito", "Salida actualizada con éxito.");
        return "redirect:/admin/salidasInventario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarSalida(@PathVariable Long id, RedirectAttributes ra) {
        SalidaInventario salidaInventario = salidaInventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salida de inventario no encontrada"));

        // Restaurar el stock de cada producto en los detalles de la salida
        for (DetalleSalida detalle : salidaInventario.getDetalles()) {
            Producto producto = detalle.getProducto();
            if (producto != null) {
                producto.setStock(producto.getStock() + detalle.getCantidad());
                productoRepository.save(producto);
            }
        }

        // Eliminar la salida de inventario
        salidaInventarioRepository.delete(salidaInventario);

        ra.addFlashAttribute("msgExito", "Salida eliminada con éxito y el stock ha sido restaurado.");
        return "redirect:/admin/salidasInventario";
    }

}
