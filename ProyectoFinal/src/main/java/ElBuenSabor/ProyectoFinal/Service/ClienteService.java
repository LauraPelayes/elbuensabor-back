package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.Cliente;

import java.util.List;

public interface ClienteService {
    Cliente findById(Long id);
    List<Cliente> findAll();
    Cliente save(Cliente cliente);
    Cliente update(Long id, Cliente cliente);
    void deleteById(Long id);
}
