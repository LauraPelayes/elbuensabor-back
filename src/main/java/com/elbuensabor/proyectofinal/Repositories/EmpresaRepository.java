package com.elbuensabor.proyectofinal.Repositories;

import com.elbuensabor.proyectofinal.Entities.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}
