package com.elbuensabor.proyectofinal.Service;

import com.elbuensabor.proyectofinal.DTO.UnidadMedidaDTO;
import com.elbuensabor.proyectofinal.Entities.UnidadMedida;

public interface UnidadMedidaService extends BaseService<UnidadMedida, Long> {
    UnidadMedida createUnidadMedida(UnidadMedidaDTO dto) throws Exception;
    UnidadMedida updateUnidadMedida(Long id, UnidadMedidaDTO dto) throws Exception;
    UnidadMedida findByDenominacion(String denominacion) throws Exception;
}