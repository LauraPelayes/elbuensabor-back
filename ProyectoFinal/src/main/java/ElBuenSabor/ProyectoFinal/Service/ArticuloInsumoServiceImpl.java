package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.ArticuloInsumo;
import ElBuenSabor.ProyectoFinal.Exceptions.ResourceNotFoundException;
import ElBuenSabor.ProyectoFinal.Repositories.ArticuloInsumoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticuloInsumoServiceImpl implements ArticuloInsumoService {

    private final ArticuloInsumoRepository articuloInsumoRepository;

    @Override
    public ArticuloInsumo findById(Long id) {
        return articuloInsumoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artículo insumo no encontrado"));
    }

    @Override
    public List<ArticuloInsumo> findAll() {
        return articuloInsumoRepository.findAll();
    }

    @Override
    public ArticuloInsumo save(ArticuloInsumo articuloInsumo) {
        return articuloInsumoRepository.save(articuloInsumo);
    }

    @Override
    public ArticuloInsumo update(Long id, ArticuloInsumo updated) {
        ArticuloInsumo actual = findById(id);

        actual.setPrecioCompra(updated.getPrecioCompra());
        actual.setStockActual(updated.getStockActual());
        actual.setStockMinimo(updated.getStockMinimo());
        actual.setEsParaElaborar(updated.getEsParaElaborar());
        actual.setDenominacion(updated.getDenominacion());
        actual.setPrecioVenta(updated.getPrecioVenta());
        actual.setCategoria(updated.getCategoria());
        actual.setUnidadMedida(updated.getUnidadMedida());
        actual.setImagen(updated.getImagen());

        return articuloInsumoRepository.save(actual);
    }

    @Override
    public void deleteById(Long id) {
        articuloInsumoRepository.deleteById(id);
    }

    @Override
    public List<ArticuloInsumo> findByStockActualLessThanEqual(Double stockMinimo) {
        return articuloInsumoRepository.findByStockActualLessThanEqual(stockMinimo);
    }
}
