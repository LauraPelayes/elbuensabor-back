package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.Localidad;
import java.util.List;

public interface LocalidadService {
    List<Localidad> findAll();
    Localidad findById(Long id);
    Localidad save(Localidad localidad);
    Localidad update(Long id, Localidad localidad);
    void deleteById(Long id);
}
