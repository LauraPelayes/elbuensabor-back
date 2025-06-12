package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.Factura;

import java.util.List; // Asegúrate de tener esta importación

public interface FacturaService {

    List<Factura> findAll();
    Factura findById(Long id);
    Factura save(Factura factura);
    Factura update(Long id, Factura factura);
    void deleteById(Long id);
}
