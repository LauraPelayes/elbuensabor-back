package com.elbuensabor.proyectofinal.Repositories;

import com.elbuensabor.proyectofinal.Entities.ArticuloManufacturado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloManufacturadoRepository extends JpaRepository<ArticuloManufacturado, Long> {
    List<ArticuloManufacturado> findByEstaDadoDeBajaFalse(); // ¡AÑADIDO!
}
