package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.Pais;

import java.util.List;

public interface PaisService {
    List<Pais> findAll();
    Pais findById(Long id);
    Pais save(Pais pais);
    Pais update(Long id, Pais pais);
    void deleteById(Long id);
}
