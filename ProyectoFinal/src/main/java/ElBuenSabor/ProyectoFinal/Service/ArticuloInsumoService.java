package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.ArticuloInsumo;

import java.util.List;

public interface ArticuloInsumoService {
    ArticuloInsumo findById(Long id);
    List<ArticuloInsumo> findAll();
    ArticuloInsumo save(ArticuloInsumo articuloInsumo);
    ArticuloInsumo update(Long id, ArticuloInsumo articuloInsumo);
    void deleteById(Long id);
    List<ArticuloInsumo> findByStockActualLessThanEqual(Double stockMinimo);
}
