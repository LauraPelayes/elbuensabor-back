package com.elbuensabor.proyectofinal.Service;

import com.elbuensabor.proyectofinal.DTO.LocalidadCreateUpdateDTO;
import com.elbuensabor.proyectofinal.DTO.LocalidadDTO; // Para respuestas
import com.elbuensabor.proyectofinal.Entities.Localidad;
import java.util.List;

public interface LocalidadService extends BaseService<Localidad, Long> {
    LocalidadDTO createLocalidad(LocalidadCreateUpdateDTO dto) throws Exception;
    LocalidadDTO updateLocalidad(Long id, LocalidadCreateUpdateDTO dto) throws Exception;
    LocalidadDTO findLocalidadById(Long id) throws Exception;
    List<LocalidadDTO> findAllLocalidades() throws Exception;
    List<LocalidadDTO> findByProvinciaId(Long provinciaId) throws Exception;
    LocalidadDTO findByNombre(String nombre) throws Exception;
}