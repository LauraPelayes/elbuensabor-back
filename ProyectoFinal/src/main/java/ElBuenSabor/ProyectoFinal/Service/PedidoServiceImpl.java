package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.DTO.*;
import ElBuenSabor.ProyectoFinal.Entities.*;
import ElBuenSabor.ProyectoFinal.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl extends BaseServiceImpl<Pedido, Long> implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final DomicilioRepository domicilioRepository;
    private final SucursalRepository sucursalRepository;
    private final ArticuloInsumoRepository articuloInsumoRepository;
    private final ArticuloManufacturadoRepository articuloManufacturadoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final FacturaRepository facturaRepository; // Para crear factura si es necesario

    // Autowired para el Mapper si decides usar uno (ej. ModelMapper)
    // private final ModelMapper modelMapper;

    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository,
                             ClienteRepository clienteRepository,
                             DomicilioRepository domicilioRepository,
                             SucursalRepository sucursalRepository,
                             ArticuloInsumoRepository articuloInsumoRepository,
                             ArticuloManufacturadoRepository articuloManufacturadoRepository,
                             DetallePedidoRepository detallePedidoRepository,
                             FacturaRepository facturaRepository
            /* ModelMapper modelMapper */) {
        super(pedidoRepository);
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.domicilioRepository = domicilioRepository;
        this.sucursalRepository = sucursalRepository;
        this.articuloInsumoRepository = articuloInsumoRepository;
        this.articuloManufacturadoRepository = articuloManufacturadoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.facturaRepository = facturaRepository;
        // this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public PedidoResponseDTO crearPedido(PedidoCreateDTO pedidoCreateDTO) throws Exception {
        try {
            Cliente cliente = clienteRepository.findById(pedidoCreateDTO.getClienteId())
                    .orElseThrow(() -> new Exception("Cliente no encontrado con ID: " + pedidoCreateDTO.getClienteId()));

            if (cliente.isEstaDadoDeBaja()){
                throw new Exception("El cliente está dado de baja y no puede realizar pedidos.");
            }

            Sucursal sucursal = sucursalRepository.findById(pedidoCreateDTO.getSucursalId())
                    .orElseThrow(() -> new Exception("Sucursal no encontrada con ID: " + pedidoCreateDTO.getSucursalId()));

            Domicilio domicilioEntrega = null;
            if (pedidoCreateDTO.getTipoEnvio() == TipoEnvio.DELIVERY) {
                if (pedidoCreateDTO.getDomicilioEntregaId() == null) {
                    throw new Exception("Se requiere domicilio de entrega para el tipo de envío DELIVERY.");
                }
                domicilioEntrega = domicilioRepository.findById(pedidoCreateDTO.getDomicilioEntregaId())
                        .orElseThrow(() -> new Exception("Domicilio de entrega no encontrado con ID: " + pedidoCreateDTO.getDomicilioEntregaId()));
                // Validar que el domicilio pertenezca al cliente (opcional, pero recomendado)
                boolean domicilioValido = cliente.getDomicilios().stream().anyMatch(d -> d.getId().equals(domicilioEntrega.getId()));
                if (!domicilioValido) {
                    throw new Exception("El domicilio de entrega no pertenece al cliente.");
                }
            }

            Pedido pedido = Pedido.builder()
                    .fechaPedido(LocalDate.now())
                    .estado(Estado.A_CONFIRMAR) // Estado inicial
                    .tipoEnvio(pedidoCreateDTO.getTipoEnvio())
                    .formaPago(pedidoCreateDTO.getFormaPago())
                    .cliente(cliente)
                    .sucursal(sucursal)
                    .domicilioEntrega(domicilioEntrega)
                    .detallesPedidos(new HashSet<>())
                    .build();

            double totalPedido = 0.0;
            double totalCostoPedido = 0.0; // Implementar cálculo de costo si es necesario
            int tiempoEstimadoTotal = 0;

            Set<DetallePedido> detalles = new HashSet<>();
            for (DetallePedidoCreateDTO detalleDTO : pedidoCreateDTO.getDetallesPedidos()) {
                if (detalleDTO.getCantidad() <= 0) {
                    throw new Exception("La cantidad en el detalle del pedido debe ser mayor a cero.");
                }
                Articulo articulo = null;
                Double precioVentaUnitario = 0.0;
                Double precioCostoUnitario = 0.0; // Implementar si se calcula el costo

                if (detalleDTO.getArticuloManufacturadoId() != null) {
                    ArticuloManufacturado manufacturado = articuloManufacturadoRepository.findById(detalleDTO.getArticuloManufacturadoId())
                            .orElseThrow(() -> new Exception("Artículo Manufacturado no encontrado con ID: " + detalleDTO.getArticuloManufacturadoId()));
                    articulo = manufacturado;
                    precioVentaUnitario = manufacturado.getPrecioVenta();
                    tiempoEstimadoTotal += manufacturado.getTiempoEstimadoMinutos() != null ? manufacturado.getTiempoEstimadoMinutos() : 0;
                    // Lógica para restar stock de insumos del manufacturado
                    for(ArticuloManufacturadoDetalle amd : manufacturado.getDetalles()){
                        ArticuloInsumo insumoComponente = amd.getArticuloInsumo();
                        double cantidadRequerida = amd.getCantidad() * detalleDTO.getCantidad();
                        if(insumoComponente.getStockActual() < cantidadRequerida){
                            throw new Exception("Stock insuficiente para el insumo: " + insumoComponente.getDenominacion() + " requerido para " + manufacturado.getDenominacion());
                        }
                        insumoComponente.setStockActual(insumoComponente.getStockActual() - cantidadRequerida);
                        articuloInsumoRepository.save(insumoComponente);
                    }

                } else if (detalleDTO.getArticuloInsumoId() != null) {
                    ArticuloInsumo insumo = articuloInsumoRepository.findById(detalleDTO.getArticuloInsumoId())
                            .orElseThrow(() -> new Exception("Artículo Insumo no encontrado con ID: " + detalleDTO.getArticuloInsumoId()));
                    articulo = insumo;
                    precioVentaUnitario = insumo.getPrecioVenta();
                    // Restar stock de insumo (si es venta directa y no para elaborar)
                    if (insumo.getStockActual() < detalleDTO.getCantidad()) {
                        throw new Exception("Stock insuficiente para el insumo: " + insumo.getDenominacion());
                    }
                    insumo.setStockActual(insumo.getStockActual() - detalleDTO.getCantidad());
                    articuloInsumoRepository.save(insumo);
                } else {
                    throw new Exception("Cada detalle del pedido debe especificar un artículo manufacturado o un insumo.");
                }

                DetallePedido detallePedido = DetallePedido.builder()
                        .cantidad(detalleDTO.getCantidad())
                        .subTotal(precioVentaUnitario * detalleDTO.getCantidad())
                        .pedido(pedido)
                        .build();

                if(articulo instanceof ArticuloManufacturado) {
                    detallePedido.setArticuloManufacturado((ArticuloManufacturado) articulo);
                } else if (articulo instanceof ArticuloInsumo) {
                    detallePedido.setArticuloInsumo((ArticuloInsumo) articulo);
                }

                detalles.add(detallePedido);
                totalPedido += detallePedido.getSubTotal();
            }

            pedido.setDetallesPedidos(detalles); // Asignar los detalles creados
            pedido.setTotal(totalPedido);
            pedido.setTotalCosto(totalCostoPedido); // Asignar costo calculado

            // Calcular hora estimada de finalización (simplificado)
            // Podría ser más complejo: considerar horario sucursal, carga de trabajo, etc.
            if (tiempoEstimadoTotal == 0 && !detalles.isEmpty()) tiempoEstimadoTotal = 15; // Un default si solo son insumos
            pedido.setHoraEstimadaFinalizacion(LocalTime.now().plusMinutes(tiempoEstimadoTotal));

            Pedido savedPedido = pedidoRepository.save(pedido);

            // Guardar detalles explícitamente si la cascada no está configurada de Pedido a DetallePedido
            // o si se prefiere mayor control. Con CascadeType.ALL en Pedido.detallesPedidos, el save de pedido los guardaría.
            // Si no, se necesitaría:
            // detalles.forEach(detallePedidoRepository::save);

            return convertToDto(savedPedido);

        } catch (Exception e) {
            throw new Exception("Error al crear el pedido: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public PedidoResponseDTO cambiarEstadoPedido(Long pedidoId, Estado nuevoEstado) throws Exception {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new Exception("Pedido no encontrado con ID: " + pedidoId));

        // Aquí podrías añadir lógica de validación de transición de estados
        // Ej: no se puede pasar de ENTREGADO a EN_COCINA
        // Ej: si se cancela, reponer stock.

        if (pedido.getEstado() == Estado.CANCELADO || pedido.getEstado() == Estado.RECHAZADO || pedido.getEstado() == Estado.ENTREGADO) {
            if (nuevoEstado != pedido.getEstado()) { // Solo permitir cambiar si el estado es el mismo (ej. re-abrir logs o similar)
                throw new Exception("No se puede cambiar el estado de un pedido que ya está " + pedido.getEstado());
            }
        }

        // Lógica de reposición de stock si se cancela/rechaza un pedido que ya había descontado stock
        if ((nuevoEstado == Estado.CANCELADO || nuevoEstado == Estado.RECHAZADO) &&
                (pedido.getEstado() != Estado.CANCELADO && pedido.getEstado() != Estado.RECHAZADO)) {
            reponerStockPorPedido(pedido);
        }

        pedido.setEstado(nuevoEstado);

        // Si el estado es ENTREGADO y formaPago es MERCADO_PAGO (o cualquier otra que requiera factura automática)
        // y aún no tiene factura, se podría generar aquí.
        if (nuevoEstado == Estado.ENTREGADO && pedido.getFactura() == null) {
            // Crear factura (lógica simplificada)
            Factura factura = Factura.builder()
                    .fechaFacturacion(LocalDate.now())
                    .formaPago(pedido.getFormaPago())
                    .totalVenta(pedido.getTotal())
                    // .mpPaymentId(), etc. se llenarían si viene de una pasarela de pago
                    .build();
            // pedido.setFactura(factura); // JPA se encarga de la FK si Factura tiene @OneToOne(mappedBy="factura") en Pedido
            // O si Pedido es dueño de la relación:
            Factura savedFactura = facturaRepository.save(factura);
            pedido.setFactura(savedFactura);

        }

        return convertToDto(pedidoRepository.save(pedido));
    }

    private void reponerStockPorPedido(Pedido pedido) throws Exception {
        for (DetallePedido detalle : pedido.getDetallesPedidos()) {
            if (detalle.getArticuloManufacturado() != null) {
                ArticuloManufacturado manufacturado = detalle.getArticuloManufacturado();
                for (ArticuloManufacturadoDetalle amd : manufacturado.getDetalles()) {
                    ArticuloInsumo insumoComponente = amd.getArticuloInsumo();
                    double cantidadAReponer = amd.getCantidad() * detalle.getCantidad();
                    insumoComponente.setStockActual(insumoComponente.getStockActual() + cantidadAReponer);
                    articuloInsumoRepository.save(insumoComponente);
                }
            } else if (detalle.getArticuloInsumo() != null) {
                ArticuloInsumo insumoVendido = detalle.getArticuloInsumo();
                insumoVendido.setStockActual(insumoVendido.getStockActual() + detalle.getCantidad());
                articuloInsumoRepository.save(insumoVendido);
            }
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> findByClienteId(Long clienteId) throws Exception { //
        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);
        return pedidos.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> findByEstado(Estado estado) throws Exception { //
        List<Pedido> pedidos = pedidoRepository.findByEstado(estado);
        return pedidos.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponseDTO findPedidoById(Long id) throws Exception {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new Exception("Pedido no encontrado con ID: " + id));
        return convertToDto(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> findAllPedidos() throws Exception {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pedidos.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // --- Private DTO Converter ---
    // Este es un ejemplo. Idealmente, usarías una librería de Mapeo como ModelMapper o MapStruct.
    private PedidoResponseDTO convertToDto(Pedido pedido) {
        if (pedido == null) return null;

        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setHoraEstimadaFinalizacion(pedido.getHoraEstimadaFinalizacion());
        dto.setTotal(pedido.getTotal());
        dto.setTotalCosto(pedido.getTotalCosto());
        dto.setEstado(pedido.getEstado());
        dto.setTipoEnvio(pedido.getTipoEnvio());
        dto.setFormaPago(pedido.getFormaPago());
        dto.setFechaPedido(pedido.getFechaPedido());

        if (pedido.getCliente() != null) {
            // Usar un ClienteSimpleDTO o el ClienteResponseDTO que ya definimos
            ClienteResponseDTO clienteDTO = new ClienteResponseDTO(); // Mapear campos de Cliente a ClienteResponseDTO
            clienteDTO.setId(pedido.getCliente().getId());
            clienteDTO.setNombre(pedido.getCliente().getNombre());
            clienteDTO.setApellido(pedido.getCliente().getApellido());
            clienteDTO.setEmail(pedido.getCliente().getEmail());
            clienteDTO.setTelefono(pedido.getCliente().getTelefono());
            // ... otros campos necesarios del cliente
            dto.setCliente(clienteDTO);
        }

        if (pedido.getDomicilioEntrega() != null) {
            // Usar DomicilioDTO
            DomicilioDTO domicilioDTO = new DomicilioDTO(); // Mapear campos
            domicilioDTO.setId(pedido.getDomicilioEntrega().getId());
            domicilioDTO.setCalle(pedido.getDomicilioEntrega().getCalle());
            domicilioDTO.setNumero(pedido.getDomicilioEntrega().getNumero());
            domicilioDTO.setCp(pedido.getDomicilioEntrega().getCp());
            if(pedido.getDomicilioEntrega().getLocalidad() != null){
                LocalidadDTO localidadDTO = new LocalidadDTO();
                localidadDTO.setId(pedido.getDomicilioEntrega().getLocalidad().getId());
                localidadDTO.setNombre(pedido.getDomicilioEntrega().getLocalidad().getNombre());
                // Mapear Provincia y Pais si es necesario en LocalidadDTO
                domicilioDTO.setLocalidad(localidadDTO);
            }
            dto.setDomicilioEntrega(domicilioDTO);
        }

        if (pedido.getSucursal() != null) {
            SucursalDTO sucursalDTO = new SucursalDTO(); // Mapear campos de Sucursal a SucursalDTO
            sucursalDTO.setId(pedido.getSucursal().getId());
            sucursalDTO.setNombre(pedido.getSucursal().getNombre());
            // ... otros campos necesarios de la sucursal
            dto.setSucursal(sucursalDTO);
        }

        if (pedido.getFactura() != null) {
            FacturaDTO facturaDTO = new FacturaDTO(); // Mapear campos de Factura a FacturaDTO
            facturaDTO.setId(pedido.getFactura().getId());
            facturaDTO.setFechaFacturacion(pedido.getFactura().getFechaFacturacion());
            facturaDTO.setTotalVenta(pedido.getFactura().getTotalVenta());
            facturaDTO.setFormaPago(pedido.getFactura().getFormaPago());
            // ... otros campos de factura
            dto.setFactura(facturaDTO);
        }

        if (pedido.getDetallesPedidos() != null) {
            dto.setDetallesPedidos(pedido.getDetallesPedidos().stream().map(detalle -> {
                DetallePedidoDTO detalleDTO = new DetallePedidoDTO();
                detalleDTO.setId(detalle.getId());
                detalleDTO.setCantidad(detalle.getCantidad());
                detalleDTO.setSubTotal(detalle.getSubTotal());
                if (detalle.getArticuloManufacturado() != null) {
                    // Mapear ArticuloManufacturado a ArticuloManufacturadoDTO (o simple DTO)
                    ArticuloManufacturadoDTO amDto = new ArticuloManufacturadoDTO();
                    amDto.setId(detalle.getArticuloManufacturado().getId());
                    amDto.setDenominacion(detalle.getArticuloManufacturado().getDenominacion());
                    amDto.setPrecioVenta(detalle.getArticuloManufacturado().getPrecioVenta());
                    //...
                    detalleDTO.setArticuloManufacturado(amDto);
                }
                if (detalle.getArticuloInsumo() != null) {
                    // Mapear ArticuloInsumo a ArticuloInsumoDTO (o simple DTO)
                    ArticuloInsumoDTO aiDto = new ArticuloInsumoDTO();
                    aiDto.setId(detalle.getArticuloInsumo().getId());
                    aiDto.setDenominacion(detalle.getArticuloInsumo().getDenominacion());
                    aiDto.setPrecioVenta(detalle.getArticuloInsumo().getPrecioVenta());
                    //...
                    detalleDTO.setArticuloInsumo(aiDto);
                }
                return detalleDTO;
            }).collect(Collectors.toSet()));
        }
        return dto;
    }
}