package com.elbuensabor.proyectofinal.Service;

import com.elbuensabor.proyectofinal.DTO.SucursalCreateUpdateDTO;
import com.elbuensabor.proyectofinal.DTO.SucursalDTO; // Para respuestas
import com.elbuensabor.proyectofinal.Entities.Sucursal;

public interface SucursalService extends BaseService<Sucursal, Long> {
    SucursalDTO createSucursal(SucursalCreateUpdateDTO dto) throws Exception;
    SucursalDTO updateSucursal(Long id, SucursalCreateUpdateDTO dto) throws Exception;
    // Métodos para obtener sucursales con sus detalles (ej. categorías, promociones)
    //List<SucursalDTO> findAllSucursalesConDetalles() throws Exception;
    //SucursalDTO findSucursalByIdConDetalles(Long id) throws Exception;
    //boolean existsById(Long id);
}