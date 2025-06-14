package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.Empresa;

import java.util.List;

public interface EmpresaService {
    List<Empresa> findAll();
    Empresa findById(Long id);
    Empresa save(Empresa empresa);
    Empresa update(Long id, Empresa empresa);
    void deleteById(Long id);
}
