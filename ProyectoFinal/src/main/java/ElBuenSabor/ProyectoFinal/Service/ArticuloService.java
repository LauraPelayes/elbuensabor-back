package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.Articulo;
import ElBuenSabor.ProyectoFinal.Entities.ArticuloInsumo;
import ElBuenSabor.ProyectoFinal.Entities.ArticuloManufacturado;
import ElBuenSabor.ProyectoFinal.DTO.ArticuloInsumoFullDTO; // Asumiendo que usarás DTOs para la creación/actualización
import ElBuenSabor.ProyectoFinal.DTO.ArticuloManufacturadoFullDTO; // Asumiendo que usarás DTOs

import java.util.List;

public interface ArticuloService extends BaseService<Articulo, Long> {

    // Métodos específicos para ArticuloInsumo
    ArticuloInsumo createArticuloInsumo(ArticuloInsumoFullDTO dto) throws Exception;
    ArticuloInsumo updateArticuloInsumo(Long id, ArticuloInsumoFullDTO dto) throws Exception;
    List<ArticuloInsumo> findAllArticulosInsumo() throws Exception;
    List<ArticuloInsumo> findArticulosInsumoByStockActualLessThanEqual(Double stockMinimo) throws Exception; //
    // Podrías añadir un método para actualizar solo el stock si es una operación común


    // Métodos específicos para ArticuloManufacturado
    ArticuloManufacturado createArticuloManufacturado(ArticuloManufacturadoFullDTO dto) throws Exception;
    ArticuloManufacturado updateArticuloManufacturado(Long id, ArticuloManufacturadoFullDTO dto) throws Exception;
    List<ArticuloManufacturado> findAllArticulosManufacturados() throws Exception;

    // Métodos generales de búsqueda que aplican a ambos (o se implementan para buscar en ambos)
    List<Articulo> findByDenominacionContainingIgnoreCase(String denominacion) throws Exception; //
    List<Articulo> findByCategoriaId(Long categoriaId) throws Exception; //

    // Métodos para borrado lógico
    void darBajaArticulo(Long id) throws Exception;
    void darAltaArticulo(Long id) throws Exception;

    // Modificar findAll para que filtre por borrado lógico
    List<Articulo> findAllActivos() throws Exception;
    List<ArticuloInsumo> findAllArticulosInsumoActivos() throws Exception;
    List<ArticuloManufacturado> findAllArticulosManufacturadosActivos() throws Exception;

    // Ajustar búsquedas si deben filtrar por activos
    List<Articulo> findByDenominacionContainingIgnoreCaseActivos(String denominacion) throws Exception;
    List<Articulo> findByCategoriaIdActivos(Long categoriaId) throws Exception;

    // Es importante considerar cómo manejar el borrado (delete).
    // El BaseService.delete(ID id) borrará por ID. Si necesitas lógica específica
    // (ej. verificar si un insumo es parte de un manufacturado antes de borrar),
    // deberías sobrescribir o crear métodos de borrado específicos.
}