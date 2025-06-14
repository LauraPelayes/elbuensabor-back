package ElBuenSabor.ProyectoFinal.Controllers;

import ElBuenSabor.ProyectoFinal.DTO.CategoriaShortDTO;
import ElBuenSabor.ProyectoFinal.DTO.CategoriaDTO;
import ElBuenSabor.ProyectoFinal.Entities.Categoria;
import ElBuenSabor.ProyectoFinal.Mappers.CategoriaMapper;
import ElBuenSabor.ProyectoFinal.Service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final CategoriaMapper categoriaMapper;

    // 🟢 Crear nueva categoría
    @PostMapping
    public ResponseEntity<CategoriaDTO> create(@RequestBody CategoriaShortDTO dto) {
        Categoria categoria = categoriaMapper.toEntity(dto);

        // Cargar manualmente la categoría padre si tiene ID
        if (dto.getCategoriaPadreId() != null) {
            Categoria padre = categoriaService.findById(dto.getCategoriaPadreId());
            categoria.setCategoriaPadre(padre);
        }

        Categoria saved = categoriaService.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaMapper.toDTO(saved));
    }

    // 🔵 Obtener todas las categorías
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> getAll() {
        List<Categoria> categorias = categoriaService.findAll();
        return ResponseEntity.ok(categoriaMapper.toDTOList(categorias));
    }

    // 🟣 Obtener categoría por ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> getById(@PathVariable Long id) {
        Categoria categoria = categoriaService.findById(id);
        return ResponseEntity.ok(categoriaMapper.toDTO(categoria));
    }

    // 🟠 Actualizar categoría
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> update(@PathVariable Long id, @RequestBody CategoriaShortDTO dto) {
        Categoria categoria = categoriaMapper.toEntity(dto);

        if (dto.getCategoriaPadreId() != null) {
            Categoria padre = categoriaService.findById(dto.getCategoriaPadreId());
            categoria.setCategoriaPadre(padre);
        }

        Categoria updated = categoriaService.update(id, categoria);
        return ResponseEntity.ok(categoriaMapper.toDTO(updated));
    }

    // 🔴 Eliminar categoría
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoriaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
