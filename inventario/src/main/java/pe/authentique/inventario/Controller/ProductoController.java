package pe.authentique.inventario.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.authentique.inventario.Repository.ProductoRepository;
import pe.authentique.inventario.model.Producto;

import java.util.List;


@Controller
public class ProductoController {
    @Autowired
    private ProductoRepository productoRepository; //objeto de acceso a datos de la tabla Curso me permite CRUD

    @GetMapping("")
    public String index(Model model)
    {
        //Obtener los ultimos 8 cursos
        List<Producto> ultimosProductos = productoRepository.findTop8ByOrderByFechaCreacionDesc();
        model.addAttribute("ultimosProductos", ultimosProductos);
        return "index";
    }

    @GetMapping("/productos")
    String getProductos(Model model, @PageableDefault(size = 8, sort = "nombre")Pageable pageable)
    {
        Page<Producto> productos = productoRepository.findAll(pageable);
        model.addAttribute("productos", productos);
        return "lista-productos";
    }

    @GetMapping("/productos/{id}")
    String getProducto(@PathVariable Integer id, Model model)
    {
        Producto producto = productoRepository.getReferenceById(id);
        model.addAttribute("producto", producto);
        return  "detalles-productos";
    }
}
