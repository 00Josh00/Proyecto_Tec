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
import pe.authentique.inventario.Repository.ProductoRepository;
import pe.authentique.inventario.model.Producto;
import pe.authentique.inventario.service.FileSystemStorateService;

@Controller
@RequestMapping("/admin/productos")
public class AdminProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private FileSystemStorateService fileSystemStorateService;

    @GetMapping("")
    String index(Model model,
                 @PageableDefault(size = 5, sort = "nombre") Pageable pageable,
                 @RequestParam(required = false) String nombre)
    {
        Page<Producto> productos;

        //Validar si el campo de titulo osea el filtro tiene valor o no
        if (nombre != null && !nombre.trim().isEmpty())
        {
            productos = productoRepository.findByNombreContaining(nombre, pageable);
        }
        else
        {
            productos = productoRepository.findAll(pageable);
        }
        model.addAttribute("productos", productos);
        return "admin/index";
    }

    @GetMapping("/nuevo")
    String nuevo(Model model)
    {
        model.addAttribute("producto", new Producto());
        return "admin/nuevo";
    }

    @PostMapping("/nuevo")
    String registrar(@Validated Producto producto,
                     BindingResult br,
                     Model model,
                     RedirectAttributes ra)
    {
        //Validaciones
        if (producto.getImagen().isEmpty()){
            br.rejectValue("imagen", "MultipartNotEmpty");
        }

        if (br.hasErrors()) //valida si existe errores en las validaciones de los campos de Curso
        {
            model.addAttribute("producto", producto);
            return "nuevo";
        }

        //1. Guardar o registrar el curso enviado desde la vista en la base de datos
        //1.1 guarda imagen
        String rutaImagen = fileSystemStorateService.store(producto.getImagen());
        producto.setRutaImagen(rutaImagen);

        productoRepository.save(producto);

        //2. Creo un mensaje flash
        ra.addFlashAttribute("msgExito", "El producto se ha registro con éxito!");

        //3. Redirecciono a la pagina de inicio
        return "redirect:/admin/productos";
    }

    //Funcionalidad: Mostrar un form o vista donde vizualice los campos y datos de
    // los cursos para que sean editados
    //Ruta URL: /editar/{id}

    @GetMapping("/editar/{id}")
    String editar(Model model, @PathVariable Integer id)
    {
        //1. Obtener los datos de la base de datos por el ID del Producto
        Producto producto= productoRepository.getReferenceById(id);

        //2. Agregar como atributo el producto al modelo
        model.addAttribute("producto", producto);

        //3. Enviar a la vista (HTML) el model
        return "admin/editar";
    }

    //Funcionalidad: Actualizar en la base de datos los registros o datos editados.
    //RUta o URL: editar/{id}

    @PostMapping("/editar/{id}")
    String actualizar(Model model, @PathVariable Integer id,@Validated Producto producto, BindingResult br, RedirectAttributes ra)
    {
        //Validaciones
        if (br.hasErrors()) //valida si existe errores en las validaciones de los campos de Producto
        {
            model.addAttribute("producto", producto);
            return "admin/editar";
        }

        //1. Obtenemos el Producto de la base datos para editarlo
        Producto productoFromDB = productoRepository.getReferenceById(id);

        //2. Setear o actualizar los campos con los nuevos valores del producto
        if (!producto.getImagen().isEmpty()){
            String rutaImagen = fileSystemStorateService.store(producto.getImagen());
            productoFromDB.setRutaImagen(rutaImagen);
        }
        productoFromDB.setDescripcion(producto.getDescripcion());
        productoFromDB.setNombre(producto.getNombre());
        productoFromDB.setPrecio(producto.getPrecio());
        productoFromDB.setComplemento(producto.getComplemento());

        //3. Actualizamos en la base de datos
        productoRepository.save(productoFromDB);

        //4. Creo un mensaje flash
        ra.addFlashAttribute("msgExito", "El producto se ha actualizado con éxito!");

        //5. redirecdionamos al pagina de inicio de productos
        return "redirect:/admin/productos";
    }

    @PostMapping("/eliminar/{id}")
    String eliminar(@PathVariable Integer id, RedirectAttributes ra)
    {
        //1. eliminamos el curso por su ID, utilizando el repository
        productoRepository.deleteById(id);

        //2. Creo un mensaje flash
        ra.addFlashAttribute("msgExito", "El producto se ha eliminado con éxito!");

        //3. redireccionamos a la página de inicio de productos
        return "redirect:/admin/productos";
    }
}