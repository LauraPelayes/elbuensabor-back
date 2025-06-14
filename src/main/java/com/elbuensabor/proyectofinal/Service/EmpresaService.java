package com.elbuensabor.proyectofinal.Service;

import com.elbuensabor.proyectofinal.DTO.EmpresaDTO; // Asumiendo que lo usarás para crear/actualizar
import com.elbuensabor.proyectofinal.Entities.Empresa;

public interface EmpresaService extends BaseService<Empresa, Long> {
    // Si necesitas métodos específicos, añádelos aquí.
    // Por ejemplo, para usar DTOs en la creación/actualización:
    Empresa createEmpresa(EmpresaDTO dto) throws Exception;
    Empresa updateEmpresa(Long id, EmpresaDTO dto) throws Exception;
}