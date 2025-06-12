package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.Promocion;
import java.util.List;

public interface PromocionService {
    List<Promocion> findAll();
    Promocion findById(Long id);
    Promocion save(Promocion promocion);
    Promocion update(Long id, Promocion promocion);
    void deleteById(Long id);
}
