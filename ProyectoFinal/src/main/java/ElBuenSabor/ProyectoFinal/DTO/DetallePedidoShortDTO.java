package ElBuenSabor.ProyectoFinal.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoShortDTO {

    private Integer cantidad;
    private Long articuloManufacturadoId;
    private Long articuloInsumoId;

}