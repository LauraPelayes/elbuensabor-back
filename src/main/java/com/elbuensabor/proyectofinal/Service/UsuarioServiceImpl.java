package com.elbuensabor.proyectofinal.Service;

import com.elbuensabor.proyectofinal.Entities.Usuario;
import com.elbuensabor.proyectofinal.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl extends BaseServiceImpl<Usuario, Long> implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        // Asegúrate de que UsuarioRepository sea JpaRepository<Usuario, Long>
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByUsername(String username) throws Exception {
        try {
            // Necesitarías añadir este método a tu UsuarioRepository:
            // Usuario findByUsername(String username);
            // Por ahora, si no existe, lo simulamos buscando todos y filtrando (no eficiente):
            return usuarioRepository.findAll().stream()
                    .filter(u -> u.getUsername() != null && u.getUsername().equals(username))
                    .findFirst();
            // Lo ideal es: return Optional.ofNullable(usuarioRepository.findByUsername(username));
        } catch (Exception e) {
            throw new Exception("Error al buscar usuario por username: " + e.getMessage(), e);
        }
    }


    @Override
    public List<Usuario> findAll() throws Exception {
        return List.of();
    }

    @Override
    public Optional<Usuario> findById(Long aLong) throws Exception {
        return Optional.empty();
    }
}