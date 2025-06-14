package com.elbuensabor.proyectofinal.Repositories;


import com.elbuensabor.proyectofinal.Entities.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadMedidaRepository extends JpaRepository<UnidadMedida, Long> {
    UnidadMedida findByDenominacion(String denominacion); // Para buscar por nombre de unidad
}
