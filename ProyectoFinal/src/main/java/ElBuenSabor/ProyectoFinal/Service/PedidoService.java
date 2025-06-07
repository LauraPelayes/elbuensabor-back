package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.DTO.PedidoCreateDTO;
// Considera un PedidoUpdateDTO si la actualización es diferente a la creación o muy limitada
import ElBuenSabor.ProyectoFinal.DTO.PedidoResponseDTO; // Para devolver datos formateados
import ElBuenSabor.ProyectoFinal.Entities.Estado;
import ElBuenSabor.ProyectoFinal.Entities.Pedido;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PedidoService extends BaseService<Pedido, Long> {

    PedidoResponseDTO crearPedido(PedidoCreateDTO pedidoCreateDTO) throws Exception;

    PedidoResponseDTO findPedidoById(Long id) throws Exception; // Para obtener un DTO específico

    List<PedidoResponseDTO> findAllPedidos() throws Exception; // Para obtener todos como DTO

    @Transactional
    PedidoResponseDTO actualizarEstadoPedido(Long pedidoId, Estado nuevoEstado) throws Exception;

    @Transactional(readOnly = true)
    List<PedidoResponseDTO> findPedidosByClienteId(Long clienteId) throws Exception;

    @Transactional(readOnly = true)
    List<PedidoResponseDTO> findPedidosByEstado(Estado estado) throws Exception;

    // Otros métodos como marcar como pagado, anular, etc.
    PedidoResponseDTO marcarPedidoComoPagado(Long pedidoId) throws Exception; // HU#15 (para efectivo)

    List<PedidoResponseDTO> findByClienteId(Long clienteId);

    List<PedidoResponseDTO> findByEstado(Estado estado);

    PedidoResponseDTO cambiarEstadoPedido(Long id, Estado nuevoEstado);
}