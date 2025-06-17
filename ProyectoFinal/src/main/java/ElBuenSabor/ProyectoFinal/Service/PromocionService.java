package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.Promocion;
import java.util.List;

public interface PromocionService extends BaseService<Promocion, Long> {
    // Nuevos métodos para manejar las promociones
    Double aplicarDescuentoCantidad(Promocion promocion, List<Long> articuloIds, Integer cantidad);
    void aplicarRegaloPromocion(Promocion promocion, List<Long> articuloIds, Integer cantidad);
    Double generarDescuentoSiguienteCompra(Promocion promocion, Double montoTotal);
    List<Promocion> getPromocionesActivas();
}