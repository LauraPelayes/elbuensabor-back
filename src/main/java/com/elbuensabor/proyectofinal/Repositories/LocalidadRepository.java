package com.elbuensabor.proyectofinal.Repositories;

import com.elbuensabor.proyectofinal.Entities.Localidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalidadRepository extends JpaRepository<Localidad, Long> {
    Localidad findByNombre(String nombre); // Para buscar una localidad por su nombre
}
