package com.elbuensabor.proyectofinal.Service;

import com.elbuensabor.proyectofinal.DTO.PromocionDTO; // Usaremos este para crear/actualizar y responder
import com.elbuensabor.proyectofinal.Entities.Promocion;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public interface PromocionService extends BaseService<Promocion, Long> {
    PromocionDTO createPromocion(PromocionDTO dto) throws Exception;
    PromocionDTO updatePromocion(Long id, PromocionDTO dto) throws Exception;
    List<PromocionDTO> findActivePromociones(LocalDate fechaActual, LocalTime horaActual) throws Exception; //
    List<PromocionDTO> findPromocionesBySucursalId(Long sucursalId) throws Exception; //
}