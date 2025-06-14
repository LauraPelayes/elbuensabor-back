package ElBuenSabor.ProyectoFinal.Controllers;

import ElBuenSabor.ProyectoFinal.DTO.EmpresaDTO;
import ElBuenSabor.ProyectoFinal.Entities.Empresa;
import ElBuenSabor.ProyectoFinal.Mappers.EmpresaMapper;
import ElBuenSabor.ProyectoFinal.Service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;
    private final EmpresaMapper empresaMapper;

    // 🟢 Crear empresa
    @PostMapping
    public ResponseEntity<EmpresaDTO> create(@RequestBody EmpresaDTO dto) {
        Empresa empresa = empresaMapper.toEntity(dto);
        Empresa saved = empresaService.save(empresa);
        return ResponseEntity.status(HttpStatus.CREATED).body(empresaMapper.toDTO(saved));
    }

    // 🔵 Obtener todas las empresas
    @GetMapping
    public ResponseEntity<List<EmpresaDTO>> getAll() {
        List<Empresa> empresas = empresaService.findAll();
        List<EmpresaDTO> dtos = empresas.stream()
                .map(empresaMapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // 🟣 Obtener empresa por ID
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDTO> getById(@PathVariable Long id) {
        Empresa empresa = empresaService.findById(id);
        return ResponseEntity.ok(empresaMapper.toDTO(empresa));
    }

    // 🟠 Actualizar empresa
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDTO> update(@PathVariable Long id, @RequestBody EmpresaDTO dto) {
        Empresa updated = empresaMapper.toEntity(dto);
        Empresa saved = empresaService.update(id, updated);
        return ResponseEntity.ok(empresaMapper.toDTO(saved));
    }

    // 🔴 Eliminar empresa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        empresaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
