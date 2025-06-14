package com.elbuensabor.proyectofinal.Service;

import com.elbuensabor.proyectofinal.DTO.DomicilioCreateUpdateDTO;
import com.elbuensabor.proyectofinal.DTO.DomicilioDTO;
import com.elbuensabor.proyectofinal.Entities.Domicilio;
import java.util.List;

public interface DomicilioService extends BaseService<Domicilio, Long> {
    DomicilioDTO createDomicilioIndependiente(DomicilioCreateUpdateDTO dto) throws Exception;
    DomicilioDTO updateDomicilioIndependiente(Long id, DomicilioCreateUpdateDTO dto) throws Exception;
    DomicilioDTO findDomicilioById(Long id) throws Exception;
    List<DomicilioDTO> findAllDomicilios() throws Exception;
    // Podrías añadir métodos para buscar domicilios por localidad, CP, etc.
    // List<DomicilioDTO> findByLocalidadId(Long localidadId) throws Exception;
}