package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.*;

import ElBuenSabor.ProyectoFinal.Repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticuloServiceImpl extends BaseServiceImpl<Articulo, Long> implements ArticuloService {

    private final ArticuloRepository articuloRepository;
    private final ArticuloInsumoRepository articuloInsumoRepository;
    private final ArticuloManufacturadoRepository articuloManufacturadoRepository;

    public ArticuloServiceImpl(ArticuloRepository articuloRepository,
                               ArticuloInsumoRepository articuloInsumoRepository,
                               ArticuloManufacturadoRepository articuloManufacturadoRepository) {
        super(articuloRepository);
        this.articuloRepository = articuloRepository;
        this.articuloInsumoRepository = articuloInsumoRepository;
        this.articuloManufacturadoRepository = articuloManufacturadoRepository;
    }

    @Override
    @Transactional
    public ArticuloInsumo createArticuloInsumo(ArticuloInsumo insumo) throws Exception {
        return articuloInsumoRepository.save(insumo);
    }

    @Override
    @Transactional
    public ArticuloInsumo updateArticuloInsumo(Long id, ArticuloInsumo insumo) throws Exception {
        if (!articuloInsumoRepository.existsById(id)) {
            throw new Exception("Artículo Insumo no encontrado con ID: " + id);
        }
        insumo.setId(id); // Aseguramos que el ID sea el correcto para la actualización
        return articuloInsumoRepository.save(insumo);
    }

    @Override
    @Transactional
    public ArticuloManufacturado createArticuloManufacturado(ArticuloManufacturado manufacturado) throws Exception {
        // Vinculamos los detalles con su padre antes de guardar
        manufacturado.getDetalles().forEach(detalle -> detalle.setArticuloManufacturado(manufacturado));
        return articuloManufacturadoRepository.save(manufacturado);
    }

    @Override
    @Transactional
    public ArticuloManufacturado updateArticuloManufacturado(Long id, ArticuloManufacturado manufacturado) throws Exception {
        if (!articuloManufacturadoRepository.existsById(id)) {
            throw new Exception("Artículo Manufacturado no encontrado con ID: " + id);
        }
        manufacturado.setId(id);
        manufacturado.getDetalles().forEach(detalle -> detalle.setArticuloManufacturado(manufacturado));
        return articuloManufacturadoRepository.save(manufacturado);
    }

    @Override public List<ArticuloInsumo> findAllArticulosInsumo() throws Exception { return articuloInsumoRepository.findAll(); }
    @Override public List<ArticuloManufacturado> findAllArticulosManufacturados() throws Exception { return articuloManufacturadoRepository.findAll(); }
    @Override public List<Articulo> findByDenominacionContainingIgnoreCase(String d) throws Exception { return articuloRepository.findByDenominacionContainingIgnoreCase(d); }
    @Override public List<Articulo> findByCategoriaId(Long id) throws Exception { return articuloRepository.findByCategoria_Id(id); }
    @Override public List<ArticuloInsumo> findArticulosBajoStockMinimo() throws Exception { return articuloInsumoRepository.findArticulosBajoStockMinimo(); }

}