package ElBuenSabor.ProyectoFinal.Controllers;

import ElBuenSabor.ProyectoFinal.DTO.PaisDTO;
import ElBuenSabor.ProyectoFinal.Entities.Pais;
import ElBuenSabor.ProyectoFinal.Mappers.PaisMapper;
import ElBuenSabor.ProyectoFinal.Service.PaisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paises")
@RequiredArgsConstructor
public class PaisController {

    private final PaisService paisService;
    private final PaisMapper paisMapper;

    @GetMapping
    public ResponseEntity<List<PaisDTO>> getAll() {
        List<PaisDTO> paises = paisService.findAll().stream()
                .map(paisMapper::toDTO)
                .toList();
        return ResponseEntity.ok(paises);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaisDTO> getById(@PathVariable Long id) {
        Pais pais = paisService.findById(id);
        return ResponseEntity.ok(paisMapper.toDTO(pais));
    }

    @PostMapping
    public ResponseEntity<PaisDTO> create(@RequestBody PaisDTO dto) {
        Pais pais = paisMapper.toEntity(dto);
        Pais saved = paisService.save(pais);
        return ResponseEntity.status(HttpStatus.CREATED).body(paisMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaisDTO> update(@PathVariable Long id, @RequestBody PaisDTO dto) {
        Pais pais = paisMapper.toEntity(dto);
        Pais updated = paisService.update(id, pais);
        return ResponseEntity.ok(paisMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paisService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
