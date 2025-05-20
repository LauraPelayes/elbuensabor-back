package ElBuenSabor.ProyectoFinal.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "imagen")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Imagen extends BaseEntity {
    private String denominacion; // Por ejemplo, la URL de la imagen


}