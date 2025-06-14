package com.elbuensabor.proyectofinal.Service;

import com.elbuensabor.proyectofinal.DTO.FacturaDTO; // Para respuestas
import com.elbuensabor.proyectofinal.Entities.Factura;
import com.elbuensabor.proyectofinal.Entities.Pedido;

import java.util.List; // Asegúrate de tener esta importación

public interface FacturaService extends BaseService<Factura, Long> {
    FacturaDTO findFacturaById(Long id) throws Exception;
    List<FacturaDTO> findAllFacturas() throws Exception;
    FacturaDTO findByPedidoId(Long pedidoId) throws Exception;

    void generarFacturaPorPedido(Pedido savedPedido);
    // Otros métodos de búsqueda que puedas necesitar (por fecha, por cliente a través del pedido, etc.)
}