package ElBuenSabor.ProyectoFinal.Controllers;

import ElBuenSabor.ProyectoFinal.DTO.UnidadMedidaDTO;
import ElBuenSabor.ProyectoFinal.Entities.UnidadMedida;
import ElBuenSabor.ProyectoFinal.Mappers.UnidadMedidaMapper;
import ElBuenSabor.ProyectoFinal.Service.UnidadMedidaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/unidades-medida")
@RequiredArgsConstructor
public class UnidadMedidaController {

    private final UnidadMedidaService unidadService;
    private final UnidadMedidaMapper unidadMapper;

    // 🟢 Crear
    @PostMapping
    public ResponseEntity<UnidadMedidaDTO> create(@RequestBody UnidadMedidaDTO dto) {
        UnidadMedida unidad = unidadMapper.toEntity(dto);
        UnidadMedida saved = unidadService.save(unidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(unidadMapper.toDTO(saved));
    }

    // 🔵 Obtener todos
    @GetMapping
    public ResponseEntity<List<UnidadMedidaDTO>> getAll() {
        List<UnidadMedida> unidades = unidadService.findAll();
        return ResponseEntity.ok(unidadMapper.toDTOList(unidades));
    }

    // 🟣 Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<UnidadMedidaDTO> getById(@PathVariable Long id) {
        UnidadMedida unidad = unidadService.findById(id);
        return ResponseEntity.ok(unidadMapper.toDTO(unidad));
    }

    // 🟠 Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<UnidadMedidaDTO> update(@PathVariable Long id, @RequestBody UnidadMedidaDTO dto) {
        UnidadMedida unidad = unidadMapper.toEntity(dto);
        UnidadMedida updated = unidadService.update(id, unidad);
        return ResponseEntity.ok(unidadMapper.toDTO(updated));
    }

    // 🔴 Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        unidadService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
