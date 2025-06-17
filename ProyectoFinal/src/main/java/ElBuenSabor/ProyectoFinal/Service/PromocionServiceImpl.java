package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.DTO.ArticuloManufacturadoDTO;
import ElBuenSabor.ProyectoFinal.DTO.ImagenDTO;
import ElBuenSabor.ProyectoFinal.DTO.PromocionDTO;
import ElBuenSabor.ProyectoFinal.DTO.SucursalDTO;
import ElBuenSabor.ProyectoFinal.Entities.*;
import ElBuenSabor.ProyectoFinal.Exceptions.ResourceNotFoundException; // Posiblemente ya no sea necesaria
import ElBuenSabor.ProyectoFinal.Repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class PromocionServiceImpl extends BaseServiceImpl<Promocion, Long> implements PromocionService {

    private final ArticuloManufacturadoRepository articuloRepo;
    private final PromocionRepository promocionRepository;

    public PromocionServiceImpl(PromocionRepository promocionRepository,
                                ArticuloManufacturadoRepository articuloRepo) {
        super(promocionRepository);
        this.promocionRepository = promocionRepository;
        this.articuloRepo = articuloRepo;
    }

    @Override
    @Transactional
    public Double aplicarDescuentoCantidad(Promocion promocion, List<Long> articuloIds, Integer cantidad) {
        if (promocion.getTipoPromocion() != TipoPromocion.DESCUENTO_CANTIDAD) {
            throw new IllegalArgumentException("La promoción no es de tipo descuento por cantidad");
        }

        if (cantidad < promocion.getCantidadMinima()) {
            return 0.0;
        }

        Double precioTotal = articuloRepo.findAllById(articuloIds)
                .stream()
                .mapToDouble(articulo -> articulo.getPrecioVenta())
                .sum();

        return (precioTotal * promocion.getPorcentajeDescuento()) / 100;
    }

    @Override
    @Transactional
    public void aplicarRegaloPromocion(Promocion promocion, List<Long> articuloIds, Integer cantidad) {
        if (promocion.getTipoPromocion() != TipoPromocion.REGALO_CANTIDAD) {
            throw new IllegalArgumentException("La promoción no es de tipo regalo por cantidad");
        }

        if (cantidad < promocion.getCantidadMinima()) {
            throw new IllegalArgumentException("No se alcanza la cantidad mínima para el regalo");
        }

        // Aquí iría la lógica para agregar el artículo de regalo al pedido
        // Por ejemplo, crear un nuevo artículo con precio 0
    }

    @Override
    @Transactional
    public Double generarDescuentoSiguienteCompra(Promocion promocion, Double montoTotal) {
        if (promocion.getTipoPromocion() != TipoPromocion.DESCUENTO_SIGUIENTE_COMPRA) {
            throw new IllegalArgumentException("La promoción no es de tipo descuento para siguiente compra");
        }

        if (montoTotal < promocion.getMontoMinimo()) {
            return 0.0;
        }

        return promocion.getPorcentajeDescuento();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Promocion> getPromocionesActivas() {
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        return ((PromocionRepository) baseRepository).findAll().stream()
                .filter(p -> !p.getBaja()) // No está dada de baja
                .filter(p -> p.getFechaDesde().isBefore(hoy) || p.getFechaDesde().equals(hoy))
                .filter(p -> p.getFechaHasta().isAfter(hoy) || p.getFechaHasta().equals(hoy))
                .filter(p -> p.getHoraDesde() == null || p.getHoraDesde().isBefore(ahora))
                .filter(p -> p.getHoraHasta() == null || p.getHoraHasta().isAfter(ahora))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Promocion update(Long id, Promocion updatedPromocion) throws Exception {
        try {
            Promocion actual = findById(id);

            actual.setDenominacion(updatedPromocion.getDenominacion());
            actual.setFechaDesde(updatedPromocion.getFechaDesde());
            actual.setFechaHasta(updatedPromocion.getFechaHasta());
            actual.setHoraDesde(updatedPromocion.getHoraDesde());
            actual.setHoraHasta(updatedPromocion.getHoraHasta());
            actual.setDescripcionDescuento(updatedPromocion.getDescripcionDescuento());
            actual.setPrecioPromocional(updatedPromocion.getPrecioPromocional());
            actual.setTipoPromocion(updatedPromocion.getTipoPromocion());
            actual.setImagen(updatedPromocion.getImagen());

            // Actualizar nuevos campos
            actual.setCantidadMinima(updatedPromocion.getCantidadMinima());
            actual.setPorcentajeDescuento(updatedPromocion.getPorcentajeDescuento());
            actual.setMontoMinimo(updatedPromocion.getMontoMinimo());
            actual.setArticuloRegalo(updatedPromocion.getArticuloRegalo());

            if (updatedPromocion.getArticulosManufacturados() != null) {
                actual.getArticulosManufacturados().clear();
                actual.getArticulosManufacturados().addAll(updatedPromocion.getArticulosManufacturados());
            }

            if (updatedPromocion.getSucursales() != null) {
                actual.getSucursales().clear();
                actual.getSucursales().addAll(updatedPromocion.getSucursales());
                actual.getSucursales().forEach(sucursal -> sucursal.getPromociones().add(actual));
            }

            return baseRepository.save(actual);
        } catch (Exception e) {
            throw new Exception("Error al actualizar la promoción: " + e.getMessage());
        }
    }
}