package pe.authentique.inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "entrada_inventario")
public class EntradaInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrada")
    private Integer id;

    @NotNull
    @ManyToOne // relación con el producto
    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto") // conecta con Producto
    private Producto producto;

    @NotNull
    @Min(1)
    private Integer cantidad;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @PrePersist
    void prePersist() {
        fechaCreacion = LocalDateTime.now();
    }
}
