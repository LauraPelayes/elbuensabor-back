package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.Articulo;
import ElBuenSabor.ProyectoFinal.Entities.ArticuloInsumo;
import ElBuenSabor.ProyectoFinal.Entities.ArticuloManufacturado;
import ElBuenSabor.ProyectoFinal.DTO.ArticuloInsumoDTO; // Asumiendo que usarás DTOs para la creación/actualización
import ElBuenSabor.ProyectoFinal.DTO.ArticuloManufacturadoDTO; // Asumiendo que usarás DTOs
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ArticuloService extends BaseService<Articulo, Long> {

    // --- Métodos para ArticuloInsumo ---
    ArticuloInsumo createArticuloInsumo(ArticuloInsumo insumo) throws Exception;
    ArticuloInsumo updateArticuloInsumo(Long id, ArticuloInsumo insumo) throws Exception;
    List<ArticuloInsumo> findAllArticulosInsumo() throws Exception;
    List<ArticuloInsumo> findArticulosBajoStockMinimo() throws Exception;

    // --- Métodos para ArticuloManufacturado ---
    ArticuloManufacturado createArticuloManufacturado(ArticuloManufacturado manufacturado) throws Exception;
    ArticuloManufacturado updateArticuloManufacturado(Long id, ArticuloManufacturado manufacturado) throws Exception;
    List<ArticuloManufacturado> findAllArticulosManufacturados() throws Exception;

    // --- Métodos Generales ---
    List<Articulo> findByDenominacionContainingIgnoreCase(String denominacion) throws Exception;
    List<Articulo> findByCategoriaId(Long categoriaId) throws Exception;
}