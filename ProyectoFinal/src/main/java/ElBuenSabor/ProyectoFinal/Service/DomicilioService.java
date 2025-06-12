package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.Domicilio;
import java.util.List;

public interface DomicilioService {
    List<Domicilio> findAll();
    Domicilio findById(Long id);
    Domicilio save(Domicilio domicilio);
    Domicilio update(Long id, Domicilio domicilio);
    void deleteById(Long id);
}
