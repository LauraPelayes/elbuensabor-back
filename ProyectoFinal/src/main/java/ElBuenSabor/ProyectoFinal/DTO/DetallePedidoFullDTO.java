package ElBuenSabor.ProyectoFinal.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoFullDTO {

    private Long id;
    private Integer cantidad;
    private Double subTotal;
    private ArticuloManufacturadoFullDTO articuloManufacturado;
    private ArticuloInsumoFullDTO articuloInsumo;
    private PedidoFullDTO pedido;

}