package com.elbuensabor.proyectofinal.Repositories;

import com.elbuensabor.proyectofinal.Entities.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaisRepository extends JpaRepository<Pais, Long> {
    Pais findByNombre(String nombre); // Para buscar un país por su nombre
}