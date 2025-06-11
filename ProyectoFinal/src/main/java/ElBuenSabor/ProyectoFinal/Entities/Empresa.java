package ElBuenSabor.ProyectoFinal.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empresa")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Empresa extends BaseEntity {

    private String nombre;
    private String razonSocial;
    private Integer cuil;

    @Builder.Default
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Sucursal> sucursales = new ArrayList<>();
}