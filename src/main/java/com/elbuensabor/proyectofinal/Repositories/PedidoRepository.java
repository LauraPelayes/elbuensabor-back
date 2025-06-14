package com.elbuensabor.proyectofinal.Repositories;

import com.elbuensabor.proyectofinal.Entities.Estado;
import com.elbuensabor.proyectofinal.Entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(Long clienteId); // Para el historial de pedidos del cliente [cite: 143]

    List<Pedido> findByEstado(Estado estado); // Para filtrar pedidos por estado [cite: 151]
}