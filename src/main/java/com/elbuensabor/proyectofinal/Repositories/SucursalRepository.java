package com.elbuensabor.proyectofinal.Repositories;

import com.elbuensabor.proyectofinal.Entities.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
}