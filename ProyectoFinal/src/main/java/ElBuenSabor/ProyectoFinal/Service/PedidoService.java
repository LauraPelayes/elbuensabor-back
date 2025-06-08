package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.DTO.PedidoShortDTO;
// Considera un PedidoUpdateDTO si la actualización es diferente a la creación o muy limitada
import ElBuenSabor.ProyectoFinal.DTO.PedidoFullDTO; // Para devolver datos formateados
import ElBuenSabor.ProyectoFinal.Entities.Estado;
import ElBuenSabor.ProyectoFinal.Entities.Pedido;

import java.util.List;

public interface PedidoService extends BaseService<Pedido, Long> {

    PedidoFullDTO crearPedido(PedidoShortDTO pedidoCreateDTO) throws Exception;

    PedidoFullDTO cambiarEstadoPedido(Long pedidoId, Estado nuevoEstado) throws Exception;

    List<PedidoFullDTO> findByClienteId(Long clienteId) throws Exception; //

    List<PedidoFullDTO> findByEstado(Estado estado) throws Exception; //

    PedidoFullDTO findPedidoById(Long id) throws Exception; // Para obtener un DTO específico

    List<PedidoFullDTO> findAllPedidos() throws Exception; // Para obtener todos como DTOs
}