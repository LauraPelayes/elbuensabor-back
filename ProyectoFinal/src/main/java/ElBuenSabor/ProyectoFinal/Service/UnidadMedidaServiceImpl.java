package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.UnidadMedida;
import ElBuenSabor.ProyectoFinal.Exceptions.ResourceNotFoundException;
import ElBuenSabor.ProyectoFinal.Repositories.UnidadMedidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnidadMedidaServiceImpl implements UnidadMedidaService {

    private final UnidadMedidaRepository unidadRepository;

    @Override
    public List<UnidadMedida> findAll() {
        return unidadRepository.findAll();
    }

    @Override
    public UnidadMedida findById(Long id) {
        return unidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidad de medida no encontrada con ID: " + id));
    }

    @Override
    public UnidadMedida save(UnidadMedida unidad) {
        return unidadRepository.save(unidad);
    }

    @Override
    public UnidadMedida update(Long id, UnidadMedida unidad) {
        UnidadMedida actual = findById(id);
        actual.setDenominacion(unidad.getDenominacion());
        return unidadRepository.save(actual);
    }

    @Override
    public void deleteById(Long id) {
        unidadRepository.deleteById(id);
    }
}
