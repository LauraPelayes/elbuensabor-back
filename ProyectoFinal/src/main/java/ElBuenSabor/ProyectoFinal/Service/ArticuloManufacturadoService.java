package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.ArticuloManufacturado;

import java.util.List;

public interface ArticuloManufacturadoService {
    ArticuloManufacturado findById(Long id);
    List<ArticuloManufacturado> findAll();
    ArticuloManufacturado save(ArticuloManufacturado articulo);
    ArticuloManufacturado update(Long id, ArticuloManufacturado updated);
    void deleteById(Long id);
}
