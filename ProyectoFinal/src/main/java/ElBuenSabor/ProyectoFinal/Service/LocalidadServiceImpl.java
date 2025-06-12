package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.Localidad;
import ElBuenSabor.ProyectoFinal.Exceptions.ResourceNotFoundException;
import ElBuenSabor.ProyectoFinal.Repositories.LocalidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalidadServiceImpl implements LocalidadService {

    private final LocalidadRepository localidadRepository;

    @Override
    public List<Localidad> findAll() {
        return localidadRepository.findAll();
    }

    @Override
    public Localidad findById(Long id) {
        return localidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Localidad no encontrada con ID: " + id));
    }

    @Override
    public Localidad save(Localidad localidad) {
        return localidadRepository.save(localidad);
    }

    @Override
    public Localidad update(Long id, Localidad localidad) {
        Localidad actual = findById(id);
        actual.setNombre(localidad.getNombre());
        actual.setProvincia(localidad.getProvincia());
        actual.setBaja(localidad.isBaja());
        return localidadRepository.save(actual);
    }

    @Override
    public void deleteById(Long id) {
        localidadRepository.deleteById(id);
    }
}
