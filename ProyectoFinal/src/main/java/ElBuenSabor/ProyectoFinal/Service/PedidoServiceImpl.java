package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.DTO.*;
import ElBuenSabor.ProyectoFinal.Entities.*;
import ElBuenSabor.ProyectoFinal.Entities.Estado;


import ElBuenSabor.ProyectoFinal.Entities.FormaPago;
import ElBuenSabor.ProyectoFinal.Entities.TipoEnvio;
import ElBuenSabor.ProyectoFinal.Exceptions.ResourceNotFoundException;
import ElBuenSabor.ProyectoFinal.Repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;

    @Override
    public Pedido findById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));
    }

    @Override
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido); // guarda Pedido + Factura + Detalles gracias a Cascade
    }

    @Override
    public Pedido update(Long id, Pedido updated) {
        Pedido actual = findById(id);

        actual.setFechaPedido(updated.getFechaPedido());
        actual.setHoraEstimadaFinalizacion(updated.getHoraEstimadaFinalizacion());
        actual.setTotal(updated.getTotal());
        actual.setTotalCosto(updated.getTotalCosto());
        actual.setEstado(updated.getEstado());
        actual.setTipoEnvio(updated.getTipoEnvio());
        actual.setFormaPago(updated.getFormaPago());

        actual.setCliente(updated.getCliente());
        actual.setEmpleado(updated.getEmpleado());
        actual.setSucursal(updated.getSucursal());
        actual.setDomicilioEntrega(updated.getDomicilioEntrega());
        actual.setFactura(updated.getFactura());
        actual.setDetallesPedidos(updated.getDetallesPedidos());

        return pedidoRepository.save(actual);
    }


    @Override
    public void deleteById(Long id) {
        pedidoRepository.deleteById(id);
    }
}
