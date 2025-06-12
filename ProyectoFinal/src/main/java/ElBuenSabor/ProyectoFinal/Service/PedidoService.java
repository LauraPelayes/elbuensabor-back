package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.Pedido;

import java.util.List;

public interface PedidoService {
    Pedido findById(Long id);
    List<Pedido> findAll();
    Pedido save(Pedido pedido);
    Pedido update(Long id, Pedido pedido);
    void deleteById(Long id);
}
