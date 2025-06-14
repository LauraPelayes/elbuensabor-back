package com.elbuensabor.proyectofinal.Service;

import com.elbuensabor.proyectofinal.DTO.ClienteActualizacionDTO;
import com.elbuensabor.proyectofinal.DTO.ClienteRegistroDTO;
import com.elbuensabor.proyectofinal.DTO.LoginDTO;
import com.elbuensabor.proyectofinal.Entities.Cliente;

public interface ClienteService extends BaseService<Cliente, Long> {
    Cliente registrarCliente(ClienteRegistroDTO registroDTO) throws Exception;
    Cliente loginCliente(LoginDTO loginDTO) throws Exception;
    Cliente actualizarCliente(Long id, ClienteActualizacionDTO actualizacionDTO) throws Exception;
    void darBajaCliente(Long id) throws Exception; //
    void darAltaCliente(Long id) throws Exception;


    // Considerar añadir un método que devuelva un ClienteResponseDTO si es necesario
    // ClienteResponseDTO findClienteByIdDTO(Long id) throws Exception;
}