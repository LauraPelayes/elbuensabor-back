package com.elbuensabor.proyectofinal.Service;

import com.elbuensabor.proyectofinal.DTO.ProvinciaCreateUpdateDTO;
import com.elbuensabor.proyectofinal.DTO.ProvinciaDTO; // Para respuestas
import com.elbuensabor.proyectofinal.Entities.Provincia;

import java.util.List;

public interface ProvinciaService extends BaseService<Provincia, Long> {
    ProvinciaDTO createProvincia(ProvinciaCreateUpdateDTO dto) throws Exception;
    ProvinciaDTO updateProvincia(Long id, ProvinciaCreateUpdateDTO dto) throws Exception;
    ProvinciaDTO findProvinciaById(Long id) throws Exception; // Para devolver DTO
    List<ProvinciaDTO> findAllProvincias() throws Exception; // Para devolver DTOs
    List<ProvinciaDTO> findByPaisId(Long paisId) throws Exception;
    ProvinciaDTO findByNombre(String nombre) throws Exception;
}
