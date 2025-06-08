package ElBuenSabor.ProyectoFinal.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "unidad_medida")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UnidadMedida extends BaseEntity {

    @Column(unique = true)
    private String denominacion; // Ej: "kg", "gr", "unidad", "litro"

}