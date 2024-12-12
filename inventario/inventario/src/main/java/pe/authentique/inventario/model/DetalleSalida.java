package pe.authentique.inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "detalle_salida")
public class DetalleSalida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "detalle_salida_seq", sequenceName = "detalle_salida_seq", allocationSize = 1)
    @Column(name = "id_detalle")
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_salida", referencedColumnName = "id_salida")
    private SalidaInventario salidaInventario;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto")
    private Producto producto;

    @NotNull
    @Min(1)
    private Integer cantidad;
}