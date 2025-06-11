package ElBuenSabor.ProyectoFinal.DTO;

import ElBuenSabor.ProyectoFinal.Entities.TipoEnvio;
import ElBuenSabor.ProyectoFinal.Entities.FormaPago;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoCreateDTO {
    //Para crear un pedido
    private Long clienteId;
    private boolean estaDadoDeBaja;
    private Long domicilioEntregaId; // Required if tipoEnvio is DELIVERY
    private TipoEnvio tipoEnvio;
    private FormaPago formaPago;
    private Long sucursalId;
    private List<DetallePedidoCreateDTO> detallesPedidos; // Simplified details for creation
    // total and totalCosto will be calculated server-side
    // horaEstimadaFinalizacion, estado, fechaPedido will be set server-side initially
}