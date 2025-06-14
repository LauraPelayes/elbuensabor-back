package com.elbuensabor.proyectofinal.Repositories;

import com.elbuensabor.proyectofinal.Entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> { // Cambiado Integer a Long
    Usuario findByUsername(String username); // Añade este método si aún no lo has hecho
}