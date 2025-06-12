package ElBuenSabor.ProyectoFinal.Controllers;

import ElBuenSabor.ProyectoFinal.DTO.ProvinciaDTO;
import ElBuenSabor.ProyectoFinal.Entities.Pais;
import ElBuenSabor.ProyectoFinal.Entities.Provincia;
import ElBuenSabor.ProyectoFinal.Exceptions.ResourceNotFoundException;
import ElBuenSabor.ProyectoFinal.Mappers.ProvinciaMapper;
import ElBuenSabor.ProyectoFinal.Repositories.PaisRepository;
import ElBuenSabor.ProyectoFinal.Service.ProvinciaService;
import ElBuenSabor.ProyectoFinal.Service.LocalidadService; // Para listar localidades
import ElBuenSabor.ProyectoFinal.DTO.LocalidadDTO; // Para la respuesta de localidades

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provincias")
@RequiredArgsConstructor
public class ProvinciaController {

    private final ProvinciaService provinciaService;
    private final ProvinciaMapper provinciaMapper;
    private final PaisRepository paisRepository;

    @GetMapping
    public ResponseEntity<List<ProvinciaDTO>> getAll() {
        List<Provincia> provincias = provinciaService.findAll();
        return ResponseEntity.ok(provinciaMapper.toDTOList(provincias));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProvinciaDTO> getById(@PathVariable Long id) {
        Provincia provincia = provinciaService.findById(id);
        return ResponseEntity.ok(provinciaMapper.toDTO(provincia));
    }

    @PostMapping
    public ResponseEntity<ProvinciaDTO> create(@RequestBody ProvinciaDTO dto) {
        Provincia provincia = provinciaMapper.toEntity(dto);

        if (dto.getPais() != null && dto.getPais().getId() != null) {
            Pais pais = paisRepository.findById(dto.getPais().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("País no encontrado"));
            provincia.setPais(pais);
        }

        Provincia saved = provinciaService.save(provincia);
        return ResponseEntity.status(HttpStatus.CREATED).body(provinciaMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProvinciaDTO> update(@PathVariable Long id, @RequestBody ProvinciaDTO dto) {
        Provincia provincia = provinciaMapper.toEntity(dto);

        if (dto.getPais() != null && dto.getPais().getId() != null) {
            Pais pais = paisRepository.findById(dto.getPais().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("País no encontrado"));
            provincia.setPais(pais);
        }

        Provincia updated = provinciaService.update(id, provincia);
        return ResponseEntity.ok(provinciaMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        provinciaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
