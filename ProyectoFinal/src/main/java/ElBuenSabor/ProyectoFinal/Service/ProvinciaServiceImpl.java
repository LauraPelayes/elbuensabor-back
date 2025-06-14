package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.Provincia;
import ElBuenSabor.ProyectoFinal.Exceptions.ResourceNotFoundException;
import ElBuenSabor.ProyectoFinal.Repositories.ProvinciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinciaServiceImpl implements ProvinciaService {

    private final ProvinciaRepository provinciaRepository;

    @Override
    public List<Provincia> findAll() {
        return provinciaRepository.findAll();
    }

    @Override
    public Provincia findById(Long id) {
        return provinciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provincia no encontrada con ID: " + id));
    }

    @Override
    public Provincia save(Provincia provincia) {
        return provinciaRepository.save(provincia);
    }

    @Override
    public Provincia update(Long id, Provincia provincia) {
        Provincia actual = findById(id);
        actual.setNombre(provincia.getNombre());
        actual.setPais(provincia.getPais());
        return provinciaRepository.save(actual);
    }

    @Override
    public void deleteById(Long id) {
        provinciaRepository.deleteById(id);
    }
}
