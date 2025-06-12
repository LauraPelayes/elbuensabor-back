package ElBuenSabor.ProyectoFinal.Controllers;

import ElBuenSabor.ProyectoFinal.Repositories.ArticuloInsumoRepository;
import ElBuenSabor.ProyectoFinal.Repositories.ArticuloManufacturadoRepository;
import ElBuenSabor.ProyectoFinal.DTO.FacturaCreateDTO;
import ElBuenSabor.ProyectoFinal.DTO.PedidoCreateDTO;
import ElBuenSabor.ProyectoFinal.DTO.PedidoDTO;
import ElBuenSabor.ProyectoFinal.Entities.*;
import ElBuenSabor.ProyectoFinal.Entities.FormaPago;
import ElBuenSabor.ProyectoFinal.Exceptions.ResourceNotFoundException;
import ElBuenSabor.ProyectoFinal.Mappers.PedidoMapper;
import ElBuenSabor.ProyectoFinal.Repositories.*;
import ElBuenSabor.ProyectoFinal.Service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Ajustalo según tu frontend
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoMapper pedidoMapper;

    private final ArticuloInsumoRepository articuloInsumoRepository;
    private final ArticuloManufacturadoRepository articuloManufacturadoRepository;
    private final ClienteRepository clienteRepository;
    private final DomicilioRepository domicilioRepository;
    private final SucursalRepository sucursalRepository;
    private final UsuarioRepository usuarioRepository;
    private final ArticuloRepository articuloRepository;

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> getAll() {
        List<Pedido> pedidos = pedidoService.findAll();
        return ResponseEntity.ok(
                pedidos.stream().map(pedidoMapper::toDTO).toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> getById(@PathVariable Long id) {
        Pedido pedido = pedidoService.findById(id);
        return ResponseEntity.ok(pedidoMapper.toDTO(pedido));
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> create(@RequestBody PedidoCreateDTO dto) {
        Pedido pedido = pedidoMapper.toEntity(dto);

        // 🧩 Asignación de relaciones obligatorias
        pedido.setCliente(clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado")));

        pedido.setDomicilioEntrega(domicilioRepository.findById(dto.getDomicilioId())
                .orElseThrow(() -> new ResourceNotFoundException("Domicilio no encontrado")));

        // 🧩 Relaciones opcionales
        if (dto.getSucursalId() != null) {
            pedido.setSucursal(sucursalRepository.findById(dto.getSucursalId()).orElse(null));
        }

        if (dto.getEmpleadoId() != null) {
            pedido.setEmpleado(usuarioRepository.findById(dto.getEmpleadoId()).orElse(null));
        }

        // 🧾 Factura
        if (dto.getFactura() != null) {
            FacturaCreateDTO f = dto.getFactura();
            Factura factura = Factura.builder()
                    .fechaFacturacion(f.getFechaFacturacion())
                    .mpPaymentId(f.getMpPaymentId())
                    .mpMerchantOrderId(f.getMpMerchantOrderId())
                    .mpPreferenceId(f.getMpPreferenceId())
                    .mpPaymentType(f.getMpPaymentType())
                    .formaPago(FormaPago.valueOf(String.valueOf(f.getFormaPago())))
                    .totalVenta(f.getTotalVenta())
                    .build();

            pedido.setFactura(factura);
        }

        // 🧾 Detalles del pedido
        if (dto.getDetalles() != null) {
            Set<DetallePedido> detalles = dto.getDetalles().stream().map(detalleDTO -> {
                DetallePedido detalle = new DetallePedido();
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setSubTotal(detalleDTO.getSubTotal());

                // Resolver el tipo de artículo según a cuál repositorio pertenezca
                ArticuloInsumo insumo = articuloInsumoRepository.findById(detalleDTO.getArticuloId()).orElse(null);
                if (insumo != null) {
                    detalle.setArticuloInsumo(insumo);
                } else {
                    ArticuloManufacturado manufacturado = articuloManufacturadoRepository.findById(detalleDTO.getArticuloId())
                            .orElseThrow(() -> new ResourceNotFoundException("Artículo no encontrado"));
                    detalle.setArticuloManufacturado(manufacturado);
                }

                detalle.setPedido(pedido); // relación inversa
                return detalle;
            }).collect(Collectors.toSet());

            pedido.setDetallesPedidos(detalles);
        }

        Pedido saved = pedidoService.save(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoMapper.toDTO(saved));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
