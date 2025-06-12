package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.ArticuloManufacturadoDetalle;

public interface ArticuloManufacturadoDetalleService {
    ArticuloManufacturadoDetalle findById(Long id);
    void deleteById(Long id);
}
