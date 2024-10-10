package pe.authentique.inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

@Data
@Entity
public class Producto {

    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //serial auto incremental
    @Column(name = "idproducto") // que el nombre de columna en la tabla sea idcurso
    private Integer id;

    @NotBlank //anotacion de validacion
    private String nombre;

    @Size(max=500) //anotacion de validacion
    private String descripcion;

    @Size(max=500) //anotacion de validacion
    private String complemento;

    private String rutaImagen;

    @Transient //este campo no va a ser persistente
    private MultipartFile imagen;

    @NotNull //anotacion de validacion
    @Min(1) //anotacion de validacion
    @Max(1000) //anotacion de validacion
    private Float precio;

    private LocalDateTime fechaCreacion;

    @Column(name="fecha_act")
    private LocalDateTime fechaActualizacion;


    private Integer stock;



    @PrePersist //se ejecuta en automatico antes de insertar, asigna la fecha del sistema al campo fecha de creacion
    void prePersist()
    {
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate // se ejecuta en automatico antes de actualizar, asigna la fecha del sistema al campo fecha de act
    void preUpdate()
    {
        fechaActualizacion = LocalDateTime.now();
    }

}
