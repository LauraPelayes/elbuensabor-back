package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.ArticuloManufacturadoDetalle;
import ElBuenSabor.ProyectoFinal.Exceptions.ResourceNotFoundException;
import ElBuenSabor.ProyectoFinal.Repositories.ArticuloManufacturadoDetalleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticuloManufacturadoDetalleServiceImpl implements ArticuloManufacturadoDetalleService {

    private final ArticuloManufacturadoDetalleRepository repository;

    @Override
    public ArticuloManufacturadoDetalle findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle no encontrado"));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
