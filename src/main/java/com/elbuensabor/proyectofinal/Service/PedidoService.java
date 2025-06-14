// src/main/java/ElBuenSabor/ProyectoFinal/Service/PedidoService.java
package com.elbuensabor.proyectofinal.Service;

import com.elbuensabor.proyectofinal.DTO.PedidoCreateDTO;
import com.elbuensabor.proyectofinal.DTO.PedidoResponseDTO;
import com.elbuensabor.proyectofinal.Entities.Estado;
import com.elbuensabor.proyectofinal.Entities.Pedido; // ¡Importa Pedido aquí!

import java.util.List;

public interface PedidoService extends BaseService<Pedido, Long> {

    // Método original para crear y devolver DTO al frontend
    PedidoResponseDTO crearPedido(PedidoCreateDTO pedidoDTO) throws Exception;

    // *** NUEVO MÉTODO: Para crear el pedido y devolver la ENTIDAD PERSISTIDA ***
    Pedido crearPedidoEntidad(PedidoCreateDTO pedidoDTO) throws Exception;


    // Métodos para buscar pedidos y devolver DTOs
    PedidoResponseDTO findPedidoById(Long id) throws Exception;
    List<PedidoResponseDTO> findAllPedidos() throws Exception;
    List<PedidoResponseDTO> findPedidosByClienteId(Long clienteId) throws Exception;
    List<PedidoResponseDTO> findPedidosByEstado(Estado estado) throws Exception;

    // Método para actualizar el estado del pedido
    PedidoResponseDTO actualizarEstadoPedido(Long pedidoId, Estado nuevoEstado) throws Exception;

    // Método para marcar pedido como pagado (ej. efectivo)
    PedidoResponseDTO marcarPedidoComoPagado(Long pedidoId) throws Exception;
}