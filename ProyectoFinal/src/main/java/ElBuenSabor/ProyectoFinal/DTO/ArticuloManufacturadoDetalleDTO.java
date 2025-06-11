package ElBuenSabor.ProyectoFinal.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloManufacturadoDetalleDTO {

    private Long id;
    private boolean estaDadoDeBaja;
    private Double cantidad;
    private Long articuloInsumoId;
    // For response, you might include more ArticuloInsumo details
    private ArticuloInsumoDTO articuloInsumo;
}