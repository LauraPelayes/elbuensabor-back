package ElBuenSabor.ProyectoFinal.DTO;

import ElBuenSabor.ProyectoFinal.Entities.TipoEnvio;
import ElBuenSabor.ProyectoFinal.Entities.FormaPago;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoShortDTO {

    //Para crear un pedido
    private Long clienteId;
    private Long domicilioEntregaId;
    private TipoEnvio tipoEnvio;
    private FormaPago formaPago;
    private Long sucursalId;
    private Set<DetallePedidoShortDTO> detallesPedidos;

}