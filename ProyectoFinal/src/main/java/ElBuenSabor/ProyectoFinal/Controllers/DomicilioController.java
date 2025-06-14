package ElBuenSabor.ProyectoFinal.Controllers;

import ElBuenSabor.ProyectoFinal.DTO.DomicilioDTO;
import ElBuenSabor.ProyectoFinal.Entities.Domicilio;
import ElBuenSabor.ProyectoFinal.Exceptions.ResourceNotFoundException;
import ElBuenSabor.ProyectoFinal.Mappers.DomicilioMapper;
import ElBuenSabor.ProyectoFinal.Repositories.LocalidadRepository;
import ElBuenSabor.ProyectoFinal.Service.DomicilioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/domicilios")
@RequiredArgsConstructor
public class DomicilioController {

    private final DomicilioService domicilioService;
    private final DomicilioMapper domicilioMapper;
    private final LocalidadRepository localidadRepository;

    @GetMapping
    public ResponseEntity<List<DomicilioDTO>> getAll() {
        List<DomicilioDTO> lista = domicilioService.findAll().stream()
                .map(domicilioMapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DomicilioDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(domicilioMapper.toDTO(domicilioService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<DomicilioDTO> create(@RequestBody DomicilioDTO dto) {
        Domicilio domicilio = domicilioMapper.toEntity(dto);
        if (dto.getLocalidad() != null && dto.getLocalidad().getId() != null) {
            domicilio.setLocalidad(
                    localidadRepository.findById(dto.getLocalidad().getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Localidad no encontrada")));
        }
        Domicilio saved = domicilioService.save(domicilio);
        return ResponseEntity.status(HttpStatus.CREATED).body(domicilioMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DomicilioDTO> update(@PathVariable Long id, @RequestBody DomicilioDTO dto) {
        Domicilio domicilio = domicilioMapper.toEntity(dto);
        if (dto.getLocalidad() != null && dto.getLocalidad().getId() != null) {
            domicilio.setLocalidad(
                    localidadRepository.findById(dto.getLocalidad().getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Localidad no encontrada")));
        }
        Domicilio updated = domicilioService.update(id, domicilio);
        return ResponseEntity.ok(domicilioMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        domicilioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
