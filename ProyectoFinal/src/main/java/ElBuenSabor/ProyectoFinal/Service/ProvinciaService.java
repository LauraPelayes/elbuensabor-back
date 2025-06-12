package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.Provincia;
import java.util.List;

public interface ProvinciaService {
    List<Provincia> findAll();
    Provincia findById(Long id);
    Provincia save(Provincia provincia);
    Provincia update(Long id, Provincia provincia);
    void deleteById(Long id);
}
