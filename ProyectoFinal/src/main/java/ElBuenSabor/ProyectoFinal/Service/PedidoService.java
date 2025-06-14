package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.DTO.PedidoCreateDTO;
import ElBuenSabor.ProyectoFinal.Entities.Pedido;

import java.util.List;

public interface PedidoService extends BaseService<Pedido, Long> {
    Pedido crearPedidoPreferenciaMP(PedidoCreateDTO dto) throws Exception;
}
