package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.UnidadMedida;

import java.util.List;

public interface UnidadMedidaService {
    List<UnidadMedida> findAll();
    UnidadMedida findById(Long id);
    UnidadMedida save(UnidadMedida unidad);
    UnidadMedida update(Long id, UnidadMedida unidad);
    void deleteById(Long id);
}
