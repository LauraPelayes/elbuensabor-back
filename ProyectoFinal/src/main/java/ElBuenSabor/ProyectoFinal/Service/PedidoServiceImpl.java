package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.DTO.*;
import ElBuenSabor.ProyectoFinal.Entities.*;
import ElBuenSabor.ProyectoFinal.Entities.Estado;


import ElBuenSabor.ProyectoFinal.Entities.FormaPago;
import ElBuenSabor.ProyectoFinal.Entities.TipoEnvio;
import ElBuenSabor.ProyectoFinal.Repositories.*;
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
public  class PedidoServiceImpl extends BaseServiceImpl<Pedido, Long> implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final DomicilioRepository domicilioRepository;
    private final SucursalRepository sucursalRepository;
    private final ArticuloManufacturadoRepository articuloManufacturadoRepository;
    private final ArticuloInsumoRepository articuloInsumoRepository;
    private final ArticuloManufacturadoDetalleRepository articuloManufacturadoDetalleRepository; // Para descontar stock de insumos
    private final FacturaService facturaService; // Para generar factura



    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository, ClienteRepository clienteRepository,
                             DomicilioRepository domicilioRepository, SucursalRepository sucursalRepository,
                             ArticuloManufacturadoRepository articuloManufacturadoRepository,
                             ArticuloInsumoRepository articuloInsumoRepository,
                             ArticuloManufacturadoDetalleRepository articuloManufacturadoDetalleRepository,
                             FacturaService facturaService) {
        super(pedidoRepository);
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.domicilioRepository = domicilioRepository;
        this.sucursalRepository = sucursalRepository;
        this.articuloManufacturadoRepository = articuloManufacturadoRepository;
        this.articuloInsumoRepository = articuloInsumoRepository;
        this.articuloManufacturadoDetalleRepository = articuloManufacturadoDetalleRepository;
        this.facturaService = facturaService;
    }

    @Override
    @Transactional
    public PedidoResponseDTO crearPedido(PedidoCreateDTO pedidoDTO) throws Exception {
        try {
            if (pedidoDTO.getDetallesPedidos() == null || pedidoDTO.getDetallesPedidos().isEmpty()) {
                throw new Exception("El pedido debe contener al menos un detalle de producto.");
            }

            // 1. Obtener entidades relacionadas
            Cliente cliente = clienteRepository.findById(pedidoDTO.getClienteId())
                    .orElseThrow(() -> new Exception("Cliente no encontrado con ID: " + pedidoDTO.getClienteId()));

            Sucursal sucursal = sucursalRepository.findById(pedidoDTO.getSucursalId())
                    .orElseThrow(() -> new Exception("Sucursal no encontrada con ID: " + pedidoDTO.getSucursalId()));

            Domicilio domicilioEntrega = null;
            if (pedidoDTO.getTipoEnvio() == TipoEnvio.DELIVERY) {
                domicilioEntrega = domicilioRepository.findById(pedidoDTO.getDomicilioEntregaId())
                        .orElseThrow(() -> new Exception("Domicilio de entrega no encontrado con ID: " + pedidoDTO.getDomicilioEntregaId()));
            }

            // 2. Crear la entidad Pedido y establecer propiedades iniciales
            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);
            pedido.setSucursal(sucursal);
            pedido.setDomicilioEntrega(domicilioEntrega);
            pedido.setTipoEnvio(pedidoDTO.getTipoEnvio());
            pedido.setFormaPago(pedidoDTO.getFormaPago());
            pedido.setFechaPedido(LocalDate.now());
            pedido.setEstado(Estado.A_CONFIRMAR); // Estado inicial (HU#12)

            Set<DetallePedido> detalles = new HashSet<>();
            double totalPedido = 0.0;
            double totalCosto = 0.0;

            // 3. Procesar detalles del pedido, calcular totales y descontar stock (HU#12)
            for (DetallePedidoCreateDTO detalleDTO : pedidoDTO.getDetallesPedidos()) {
                Articulo articuloBase = null;
                double precioUnitario = 0.0;
                double costoUnitario = 0.0;
                int cantidad = detalleDTO.getCantidad();

                // Validar que al menos un ID de artículo esté presente
                if (detalleDTO.getArticuloManufacturadoId() != null) {
                    articuloBase = articuloManufacturadoRepository.findById(detalleDTO.getArticuloManufacturadoId())
                            .orElseThrow(() -> new Exception("Artículo manufacturado no encontrado con ID: " + detalleDTO.getArticuloManufacturadoId()));
                    precioUnitario = articuloBase.getPrecioVenta();

                    // Calcular costo y descontar stock para ArticuloManufacturado
                    ArticuloManufacturado manufacturado = (ArticuloManufacturado) articuloBase;
                    if (manufacturado.getDetalles() == null || manufacturado.getDetalles().isEmpty()) {
                        throw new Exception("El artículo manufacturado " + manufacturado.getDenominacion() + " no tiene detalles de ingredientes configurados.");
                    }
                    double costoManufacturado = 0.0;
                    for (ArticuloManufacturadoDetalle amd : manufacturado.getDetalles()) {
                        ArticuloInsumo insumoParaDescontar = amd.getArticuloInsumo();
                        double cantidadInsumoNecesaria = amd.getCantidad() * cantidad;

                        if (insumoParaDescontar.getStockActual() < cantidadInsumoNecesaria) {
                            throw new Exception("Stock insuficiente para el ingrediente " + insumoParaDescontar.getDenominacion() + ". Stock actual: " + insumoParaDescontar.getStockActual() + ", Necesario: " + cantidadInsumoNecesaria);
                        }
                        insumoParaDescontar.setStockActual(insumoParaDescontar.getStockActual() - cantidadInsumoNecesaria);
                        articuloInsumoRepository.save(insumoParaDescontar); // Actualizar stock
                        costoManufacturado += amd.getCantidad() * insumoParaDescontar.getPrecioCompra();
                    }
                    costoUnitario = costoManufacturado; // Costo de una unidad manufacturada

                } else if (detalleDTO.getArticuloInsumoId() != null) {
                    articuloBase = articuloInsumoRepository.findById(detalleDTO.getArticuloInsumoId())
                            .orElseThrow(() -> new Exception("Artículo insumo no encontrado con ID: " + detalleDTO.getArticuloInsumoId()));
                    precioUnitario = articuloBase.getPrecioVenta();
                    costoUnitario = ((ArticuloInsumo) articuloBase).getPrecioCompra();

                    // Descontar stock para ArticuloInsumo (si es vendible directamente)
                    ArticuloInsumo insumoDirecto = (ArticuloInsumo) articuloBase;
                    if (insumoDirecto.getEsParaElaborar() != null && !insumoDirecto.getEsParaElaborar()) { // Si es una bebida, por ejemplo
                        if (insumoDirecto.getStockActual() < cantidad) {
                            throw new Exception("Stock insuficiente para el artículo " + insumoDirecto.getDenominacion() + ". Stock actual: " + insumoDirecto.getStockActual() + ", Necesario: " + cantidad);
                        }
                        insumoDirecto.setStockActual(insumoDirecto.getStockActual() - cantidad);
                        articuloInsumoRepository.save(insumoDirecto); // Actualizar stock
                    }
                } else {
                    throw new Exception("Detalle de pedido sin artículo manufacturado ni insumo especificado.");
                }

                double subTotalDetalle = cantidad * precioUnitario;
                double subCostoDetalle = cantidad * costoUnitario;

                DetallePedido detalle = new DetallePedido();
                detalle.setCantidad(cantidad);
                detalle.setSubTotal(subTotalDetalle);
                detalle.setPedido(pedido); // Establecer la relación bidireccional

                if (articuloBase instanceof ArticuloManufacturado) {
                    detalle.setArticuloManufacturado((ArticuloManufacturado) articuloBase);
                } else if (articuloBase instanceof ArticuloInsumo) {
                    detalle.setArticuloInsumo((ArticuloInsumo) articuloBase);
                }
                detalles.add(detalle); // Añadir a la colección

                totalPedido += subTotalDetalle;
                totalCosto += subCostoDetalle;
            }

            pedido.setDetallesPedidos(detalles); // Asignar la colección de detalles al pedido
            pedido.setTotal(totalPedido);
            pedido.setTotalCosto(totalCosto);
            pedido.setHoraEstimadaFinalizacion(calcularTiempoEstimadoPedido(pedido)); // Calcular tiempo estimado (HU#12)

            // 4. Guardar el pedido en la base de datos
            Pedido savedPedido = pedidoRepository.save(pedido);

            // 5. Generar factura si es Mercado Pago (HU#12)
            if (pedidoDTO.getFormaPago() == FormaPago.MERCADO_PAGO) {
                facturaService.generarFacturaPorPedido(savedPedido);
            }

            // 6. Mapear y devolver PedidoResponseDTO
            return mapToPedidoResponseDTO(savedPedido);

        } catch (Exception e) {
            throw new Exception("Error al crear el pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public PedidoResponseDTO findPedidoById(Long id) throws Exception {
        return null;
    }

    @Override
    public List<PedidoResponseDTO> findAllPedidos() throws Exception {
        return List.of();
    }

    @Transactional
    @Override
    public PedidoResponseDTO actualizarEstadoPedido(Long pedidoId, Estado nuevoEstado) throws Exception {
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(pedidoId);
        if (!pedidoOptional.isPresent()) {
            throw new Exception("Pedido no encontrado con ID: " + pedidoId);
        }
        Pedido pedido = pedidoOptional.get();

        // Lógica de coherencia de estados (HU#15)
        if (!esCambioDeEstadoValido(pedido.getEstado(), nuevoEstado)) {
            throw new Exception("Transición de estado inválida de " + pedido.getEstado() + " a " + nuevoEstado);
        }

        // Validar si el pedido está pagado antes de pasar a "Entregado" si es Efectivo (HU#15)
        if (nuevoEstado == Estado.ENTREGADO && pedido.getFormaPago() == FormaPago.EFECTIVO && pedido.getFactura() == null) {
            throw new Exception("No se puede marcar como entregado si el pago es en efectivo y no se ha marcado como cobrado.");
        }

        pedido.setEstado(nuevoEstado);
        Pedido updatedPedido = pedidoRepository.save(pedido);
        return mapToPedidoResponseDTO(updatedPedido);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PedidoResponseDTO> findPedidosByClienteId(Long clienteId) throws Exception {
        try {
            List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);
            return pedidos.stream()
                    .map(this::mapToPedidoResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener historial de pedidos para cliente " + clienteId + ": " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<PedidoResponseDTO> findPedidosByEstado(Estado estado) throws Exception {
        try {
            List<Pedido> pedidos = pedidoRepository.findByEstado(estado);
            return pedidos.stream()
                    .map(this::mapToPedidoResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener pedidos por estado " + estado + ": " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public PedidoResponseDTO marcarPedidoComoPagado(Long pedidoId) throws Exception {
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(pedidoId);
        if (!pedidoOptional.isPresent()) {
            throw new Exception("Pedido no encontrado con ID: " + pedidoId);
        }
        Pedido pedido = pedidoOptional.get();

        if (pedido.getFactura() != null) {
            throw new Exception("El pedido con ID " + pedidoId + " ya tiene una factura y ha sido marcado como pagado.");
        }
        if (pedido.getFormaPago() == FormaPago.MERCADO_PAGO) {
            throw new Exception("Este pedido fue pagado por Mercado Pago. La factura se generó automáticamente.");
        }

        // Generar factura para pagos en efectivo (HU#18)
        facturaService.generarFacturaPorPedido(pedido); // Este método actualiza el pedido con la factura.
        Pedido updatedPedido = pedidoRepository.save(pedido); // Persistir el pedido con la factura asociada.
        return mapToPedidoResponseDTO(updatedPedido);
    }

    @Override
    public List<PedidoResponseDTO> findByClienteId(Long clienteId) {
        return List.of();
    }

    @Override
    public List<PedidoResponseDTO> findByEstado(Estado estado) {
        return List.of();
    }

    @Override
    public PedidoResponseDTO cambiarEstadoPedido(Long id, Estado nuevoEstado) {
        return null;
    }


// --- Métodos Helper para lógica interna y mapeo a DTOs ---

    private LocalTime calcularTiempoEstimadoPedido(Pedido pedido) {
        int maxTiempoArticulo = 0;
        for (DetallePedido detalle : pedido.getDetallesPedidos()) {
            if (detalle.getArticuloManufacturado() != null) {
                maxTiempoArticulo = Math.max(maxTiempoArticulo, detalle.getArticuloManufacturado().getTiempoEstimadoMinutos());
            }
        }

        // Tiempo de pedidos en cocina (simplificado)
        int tiempoPedidosEnCocina = 0;
        try {
            List<Pedido> pedidosEnCocina = pedidoRepository.findByEstado(Estado.EN_COCINA);
            if (pedidosEnCocina != null && !pedidosEnCocina.isEmpty()) {
                Optional<LocalTime> maxHoraEstimada = pedidosEnCocina.stream()
                        .map(Pedido::getHoraEstimadaFinalizacion)
                        .filter(java.util.Objects::nonNull)
                        .max(LocalTime::compareTo);
                if (maxHoraEstimada.isPresent()) {
                    long minutosPendientes = LocalTime.now().until(maxHoraEstimada.get(), java.time.temporal.ChronoUnit.MINUTES);
                    tiempoPedidosEnCocina = (int) Math.max(0, minutosPendientes);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al calcular tiempo estimado de pedidos en cocina: " + e.getMessage());
            // No lanzar excepción, solo loguear y continuar con 0 si falla
        }


        int tiempoTotalEstimadoMinutos = maxTiempoArticulo + tiempoPedidosEnCocina;

        if (pedido.getTipoEnvio() == TipoEnvio.DELIVERY) {
            tiempoTotalEstimadoMinutos += 10; // 10 minutos de entrega por delivery
        }
        if (tiempoTotalEstimadoMinutos < 0) {
            tiempoTotalEstimadoMinutos = 0;
        }

        return LocalTime.now().plusMinutes(tiempoTotalEstimadoMinutos);
    }

    private boolean esCambioDeEstadoValido(Estado estadoActual, Estado nuevoEstado) {
        switch (estadoActual) {
            case A_CONFIRMAR:
                return nuevoEstado == Estado.EN_COCINA || nuevoEstado == Estado.LISTO || nuevoEstado == Estado.CANCELADO || nuevoEstado == Estado.RECHAZADO;
            case EN_COCINA:
                return nuevoEstado == Estado.LISTO || nuevoEstado == Estado.CANCELADO || nuevoEstado == Estado.RECHAZADO;
            case LISTO:
                return nuevoEstado == Estado.EN_DELIVERY || nuevoEstado == Estado.ENTREGADO || nuevoEstado == Estado.CANCELADO || nuevoEstado == Estado.RECHAZADO;
            case EN_DELIVERY:
                return nuevoEstado == Estado.ENTREGADO || nuevoEstado == Estado.RECHAZADO;
            case ENTREGADO:
            case CANCELADO:
            case RECHAZADO:
                return false;
            default:
                return false;
        }
    }


// ... (código de la clase PedidoServiceImpl) ...

    private PedidoResponseDTO mapToPedidoResponseDTO(Pedido pedido) {
        if (pedido == null) return null;

        PedidoResponseDTO dto = new PedidoResponseDTO();
        // ... (mapeo de campos directos del pedido) ...

        // Mapear Cliente
        if (pedido.getCliente() != null) {
            Cliente cli = pedido.getCliente();
            dto.setCliente(new ClienteResponseDTO(
                    cli.getId(),
                    cli.getNombre(),
                    cli.getApellido(),
                    cli.getTelefono(),
                    cli.getEmail(),
                    cli.getFechaNacimiento(),
                    cli.getUsername(),
                    cli.getAuth0Id(),
                    cli.getImagen() != null ? new ImagenDTO(cli.getImagen().getId(), cli.getImagen().getDenominacion()) : null,
                    cli.getDomicilios() != null ? cli.getDomicilios().stream().map(dom -> new DomicilioDTO(dom.getId(), dom.getCalle(), dom.getNumero(), dom.getCp(), dom.getLocalidad() != null ? dom.getLocalidad().getId() : null, dom.getLocalidad() != null ? new LocalidadDTO(dom.getLocalidad().getId(), dom.getLocalidad().getNombre(), dom.getLocalidad().getProvincia() != null ? dom.getLocalidad().getProvincia().getId() : null, dom.getLocalidad().getProvincia() != null ? new ProvinciaDTO(dom.getLocalidad().getProvincia().getId(), dom.getLocalidad().getProvincia().getNombre(), dom.getLocalidad().getProvincia().getPais() != null ? dom.getLocalidad().getProvincia().getPais().getId() : null, dom.getLocalidad().getProvincia().getPais() != null ? new PaisDTO(dom.getLocalidad().getProvincia().getPais().getId(), dom.getLocalidad().getProvincia().getPais().getNombre()) : null) : null) : null)).collect(Collectors.toList()) : null,
                    cli.isEstaDadoDeBaja()
            ));
        }

        // Mapear Domicilio de Entrega (CORRECCIÓN CLAVE)
        if (pedido.getDomicilioEntrega() != null) {
            Domicilio dom = pedido.getDomicilioEntrega();
            dto.setDomicilioEntrega(new DomicilioDTO( // DomicilioDTO tiene 6 campos: id, calle, numero, cp, localidadId, localidad
                    dom.getId(),
                    dom.getCalle(),
                    dom.getNumero(),
                    dom.getCp(),
                    dom.getLocalidad() != null ? dom.getLocalidad().getId() : null, // localidadId
                    // Mapear LocalidadDTO completa
                    dom.getLocalidad() != null ? new LocalidadDTO(
                            dom.getLocalidad().getId(),
                            dom.getLocalidad().getNombre(),
                            dom.getLocalidad().getProvincia() != null ? dom.getLocalidad().getProvincia().getId() : null, // provinciaId
                            // Mapear ProvinciaDTO completa
                            dom.getLocalidad().getProvincia() != null ? new ProvinciaDTO(
                                    dom.getLocalidad().getProvincia().getId(),
                                    dom.getLocalidad().getProvincia().getNombre(),
                                    dom.getLocalidad().getProvincia().getPais() != null ? dom.getLocalidad().getProvincia().getPais().getId() : null, // paisId
                                    // Mapear PaisDTO completa
                                    dom.getLocalidad().getProvincia().getPais() != null ? new PaisDTO(dom.getLocalidad().getProvincia().getPais().getId(), dom.getLocalidad().getProvincia().getPais().getNombre()) : null
                            ) : null
                    ) : null
            ));
        }

        // Mapear Factura (se mantiene)
        if (pedido.getFactura() != null) {
            dto.setFactura(new FacturaDTO(
                    pedido.getFactura().getId(),
                    pedido.getFactura().getFechaFacturacion(),
                    pedido.getFactura().getMpPaymentId(),
                    pedido.getFactura().getMpMerchantOrderId(),
                    pedido.getFactura().getMpPreferenceId(),
                    pedido.getFactura().getMpPaymentType(),
                    pedido.getFactura().getFormaPago(),
                    pedido.getFactura().getTotalVenta(),
                    pedido.getId()
            ));
        }

        // Mapear Sucursal (CORRECCIÓN CLAVE)
        if (pedido.getSucursal() != null) {
            Sucursal suc = pedido.getSucursal();
            dto.setSucursal(new SucursalDTO( // SucursalDTO tiene 9 campos
                    suc.getId(),
                    suc.getNombre(),
                    suc.getHorarioApertura(),
                    suc.getHorarioCierre(),
                    // Mapear DomicilioDTO completo de la sucursal (si existe)
                    suc.getDomicilio() != null ? new DomicilioDTO(
                            suc.getDomicilio().getId(),
                            suc.getDomicilio().getCalle(),
                            suc.getDomicilio().getNumero(),
                            suc.getDomicilio().getCp(),
                            suc.getDomicilio().getLocalidad() != null ? suc.getDomicilio().getLocalidad().getId() : null, // localidadId
                            // Mapear LocalidadDTO completa
                            suc.getDomicilio().getLocalidad() != null ? new LocalidadDTO(
                                    suc.getDomicilio().getLocalidad().getId(),
                                    suc.getDomicilio().getLocalidad().getNombre(),
                                    suc.getDomicilio().getLocalidad().getProvincia() != null ? suc.getDomicilio().getLocalidad().getProvincia().getId() : null, // provinciaId
                                    // Mapear ProvinciaDTO completa
                                    suc.getDomicilio().getLocalidad().getProvincia() != null ? new ProvinciaDTO(
                                            suc.getDomicilio().getLocalidad().getProvincia().getId(),
                                            suc.getDomicilio().getLocalidad().getProvincia().getNombre(),
                                            suc.getDomicilio().getLocalidad().getProvincia().getPais() != null ? suc.getDomicilio().getLocalidad().getProvincia().getPais().getId() : null, // paisId
                                            // Mapear PaisDTO completa
                                            suc.getDomicilio().getLocalidad().getProvincia().getPais() != null ? new PaisDTO(suc.getDomicilio().getLocalidad().getProvincia().getPais().getId(), suc.getDomicilio().getLocalidad().getProvincia().getPais().getNombre()) : null
                                    ) : null
                            ) : null
                    ) : null,
                    // ID del domicilio (Long)
                    suc.getDomicilio() != null ? suc.getDomicilio().getId() : null,
                    // ID de la empresa (Long)
                    suc.getEmpresa() != null ? suc.getEmpresa().getId() : null,
                    // Lista de IDs de categorías (List<Long>)
                    suc.getCategorias() != null ? suc.getCategorias().stream().map(Categoria::getId).collect(Collectors.toList()) : null,
                    // Lista de CategoriaDTOs (List<CategoriaDTO>)
                    suc.getCategorias() != null ? suc.getCategorias().stream().map(cat -> new CategoriaDTO(cat.getId(), cat.getDenominacion(), cat.getCategoriaPadre() != null ? cat.getCategoriaPadre().getId() : null, cat.getSucursales().stream().map(s -> s.getId()).collect(Collectors.toList()))).collect(Collectors.toList()) : null//error aca Cannot resolve constructor 'CategoriaDTO(Long, String, Long, R)'
            ));
        }

        // ... (Mapeo de Detalles del Pedido, ArticuloManufacturadoDTO, ArticuloInsumoDTO - se mantienen y deben ser consistentes) ...

        return dto;
    }

    @Override
    public List<Pedido> findAll() throws Exception {
        return List.of();
    }

    @Override
    public Optional<Pedido> findById(Long aLong) throws Exception {
        return Optional.empty();
    }
}