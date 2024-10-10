package pe.authentique.inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Integer id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellidos;

    @Column(name="nom_completo")
    private String nombreCompleto;

    @NotEmpty
    @Email
    private String email;

    private String password;
    private Rol rol;

    @NotBlank
    @Transient //que no se persistemte, es decir no crea la columna en la tabla en db
    private String password1;

    @NotBlank
    @Transient //que no se persistemte, es decir no crea la columna en la tabla en db
    private String password2;
    public enum Rol{
        ADMIN,
        ESTUDIANTE
    }

    @PrePersist
    @PreUpdate
    void asignarNombreCompleto()
    {
        nombreCompleto = nombre + " " + apellidos;
    }
}
