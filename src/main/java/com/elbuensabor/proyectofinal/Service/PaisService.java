package com.elbuensabor.proyectofinal.Service;

import com.elbuensabor.proyectofinal.DTO.PaisDTO;
import com.elbuensabor.proyectofinal.Entities.Pais;

public interface PaisService extends BaseService<Pais, Long> {
    Pais createPais(PaisDTO dto) throws Exception;
    Pais updatePais(Long id, PaisDTO dto) throws Exception;
    Pais findByNombre(String nombre) throws Exception;
}