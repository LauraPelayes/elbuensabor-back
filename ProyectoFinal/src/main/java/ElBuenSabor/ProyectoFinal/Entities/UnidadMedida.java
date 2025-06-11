package ElBuenSabor.ProyectoFinal.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "unidad_medida")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UnidadMedida extends BaseEntity {
    @Column(unique = true)
    private String denominacion; // Ej: "kg", "gr", "unidad", "litro"
}