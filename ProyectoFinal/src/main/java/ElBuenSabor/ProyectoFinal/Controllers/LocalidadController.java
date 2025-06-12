package ElBuenSabor.ProyectoFinal.Controllers;

import ElBuenSabor.ProyectoFinal.DTO.LocalidadCreateUpdateDTO;
import ElBuenSabor.ProyectoFinal.DTO.LocalidadDTO;
import ElBuenSabor.ProyectoFinal.Entities.Localidad;
import ElBuenSabor.ProyectoFinal.Entities.Provincia;
import ElBuenSabor.ProyectoFinal.Exceptions.ResourceNotFoundException;
import ElBuenSabor.ProyectoFinal.Mappers.LocalidadMapper;
import ElBuenSabor.ProyectoFinal.Repositories.ProvinciaRepository;
import ElBuenSabor.ProyectoFinal.Service.LocalidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/localidades")
@RequiredArgsConstructor
public class LocalidadController {

    private final LocalidadService localidadService;
    private final LocalidadMapper localidadMapper;
    private final ProvinciaRepository provinciaRepository;

    @GetMapping
    public ResponseEntity<List<LocalidadDTO>> getAll() {
        List<Localidad> localidades = localidadService.findAll();
        return ResponseEntity.ok(localidades.stream()
                .map(localidadMapper::toDTO)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocalidadDTO> getById(@PathVariable Long id) {
        Localidad localidad = localidadService.findById(id);
        return ResponseEntity.ok(localidadMapper.toDTO(localidad));
    }

    @PostMapping
    public ResponseEntity<LocalidadDTO> create(@RequestBody LocalidadCreateUpdateDTO dto) {
        Provincia provincia = provinciaRepository.findById(dto.getProvinciaId())
                .orElseThrow(() -> new ResourceNotFoundException("Provincia no encontrada"));

        Localidad localidad = new Localidad();
        localidad.setNombre(dto.getNombre());
        localidad.setProvincia(provincia);
        localidad.setBaja(dto.isEstaDadoDeBaja());

        Localidad saved = localidadService.save(localidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(localidadMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocalidadDTO> update(@PathVariable Long id, @RequestBody LocalidadCreateUpdateDTO dto) {
        Provincia provincia = provinciaRepository.findById(dto.getProvinciaId())
                .orElseThrow(() -> new ResourceNotFoundException("Provincia no encontrada"));

        Localidad localidad = new Localidad();
        localidad.setNombre(dto.getNombre());
        localidad.setProvincia(provincia);
        localidad.setBaja(dto.isEstaDadoDeBaja());

        Localidad updated = localidadService.update(id, localidad);
        return ResponseEntity.ok(localidadMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        localidadService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
