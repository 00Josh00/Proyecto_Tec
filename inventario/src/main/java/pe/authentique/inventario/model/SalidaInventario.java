package pe.authentique.inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "salida_inventario") // Nombre personalizado para la tabla
public class SalidaInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "salida_seq", sequenceName = "salida_seq", allocationSize = 1)
    @Column(name = "id_salida")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @NotNull
    private Integer cantidad; // cantidad de salida

    private LocalDateTime fechaCreacion;

    @PrePersist
    void prePersist() {
        fechaCreacion = LocalDateTime.now();
    }
}
