package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.Domicilio;
import ElBuenSabor.ProyectoFinal.Exceptions.ResourceNotFoundException;
import ElBuenSabor.ProyectoFinal.Repositories.DomicilioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DomicilioServiceImpl implements DomicilioService {

    private final DomicilioRepository domicilioRepository;

    @Override
    public List<Domicilio> findAll() {
        return domicilioRepository.findAll();
    }

    @Override
    public Domicilio findById(Long id) {
        return domicilioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Domicilio no encontrado con ID: " + id));
    }

    @Override
    public Domicilio save(Domicilio domicilio) {
        return domicilioRepository.save(domicilio);
    }

    @Override
    public Domicilio update(Long id, Domicilio domicilio) {
        Domicilio actual = findById(id);
        actual.setCalle(domicilio.getCalle());
        actual.setNumero(domicilio.getNumero());
        actual.setCp(domicilio.getCp());
        actual.setLocalidad(domicilio.getLocalidad());
        return domicilioRepository.save(actual);
    }

    @Override
    public void deleteById(Long id) {
        domicilioRepository.deleteById(id);
    }
}
