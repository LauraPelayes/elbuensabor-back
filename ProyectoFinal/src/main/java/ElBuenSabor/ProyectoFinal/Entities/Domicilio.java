package ElBuenSabor.ProyectoFinal.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "domicilio")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Domicilio extends BaseEntity {

    private String calle;
    private Integer numero;
    private Integer cp; // Código Postal

    @ManyToOne
    @JoinColumn(name = "localidad_id")
    private Localidad localidad;

    @Builder.Default
    @OneToMany(mappedBy = "domicilio", cascade = CascadeType.ALL, orphanRemoval = true) // <--- ¡Aquí está el error! le cambie domicilioEntrega a domicilio
    private Set<Pedido> pedidos = new HashSet<>();

    @Builder.Default // <-- AÑADE BUILDER.DEFAULT
    @OneToMany(mappedBy = "domicilio", cascade = CascadeType.ALL)
    private Set<Cliente> clientes = new HashSet<>();

}
