package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.Cliente;

public interface ClienteService extends BaseService<Cliente, Long> {
    Cliente registrarCliente(ClienteRegistroDTO registroDTO) throws Exception;
    Cliente loginCliente(LoginDTO loginDTO) throws Exception;
    Cliente actualizarCliente(Long id, ClienteActualizacionDTO actualizacionDTO) throws Exception;
    void darBajaCliente(Long id) throws Exception; // Para el administrador [cite: 84]
    void darAltaCliente(Long id) throws Exception; // Para el administrador
}