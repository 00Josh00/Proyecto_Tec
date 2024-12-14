package pe.authentique.inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "salida_inventario")
public class SalidaInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "salida_seq", sequenceName = "salida_seq", allocationSize = 1)
    @Column(name = "id_salida")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    private Cliente cliente;

    @Column(name = "fecha_salida", nullable = false)
    private LocalDateTime fechaSalida;

    @Setter
    @Getter
    @OneToMany(mappedBy = "salidaInventario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleSalida> detalles = new ArrayList<>();

    @PrePersist
    void prePersist() {
        fechaSalida = LocalDateTime.now();
    }

    // Métodos auxiliares para manejo de la relación
    public void addDetalle(DetalleSalida detalle) {
        detalles.add(detalle);
        detalle.setSalidaInventario(this);
    }

    public void removeDetalle(DetalleSalida detalle) {
        detalles.remove(detalle);
        detalle.setSalidaInventario(null);
    }

}
