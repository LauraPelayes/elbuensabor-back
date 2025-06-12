package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.Sucursal;

import java.util.List;

public interface SucursalService {
    List<Sucursal> findAll();
    Sucursal findById(Long id);
    Sucursal save(Sucursal sucursal);
    Sucursal update(Long id, Sucursal sucursal);
    void deleteById(Long id);
}
