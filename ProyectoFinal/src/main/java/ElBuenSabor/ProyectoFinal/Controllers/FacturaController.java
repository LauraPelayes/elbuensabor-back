package ElBuenSabor.ProyectoFinal.Controllers;

import ElBuenSabor.ProyectoFinal.DTO.FacturaCreateDTO;
import ElBuenSabor.ProyectoFinal.DTO.FacturaDTO;
import ElBuenSabor.ProyectoFinal.Entities.Factura;
import ElBuenSabor.ProyectoFinal.Mappers.FacturaMapper;
import ElBuenSabor.ProyectoFinal.Service.FacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;
    private final FacturaMapper facturaMapper;

    // 🟢 Crear nueva factura
    @PostMapping
    public ResponseEntity<FacturaDTO> create(@RequestBody FacturaCreateDTO dto) {
        Factura factura = facturaMapper.toEntity(dto);
        Factura saved = facturaService.save(factura);
        return ResponseEntity.status(HttpStatus.CREATED).body(facturaMapper.toDTO(saved));
    }

    // 🔵 Obtener todas las facturas
    @GetMapping
    public ResponseEntity<List<FacturaDTO>> getAll() {
        List<Factura> facturas = facturaService.findAll();
        List<FacturaDTO> dtos = facturas.stream()
                .map(facturaMapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // 🟣 Obtener factura por ID
    @GetMapping("/{id}")
    public ResponseEntity<FacturaDTO> getById(@PathVariable Long id) {
        Factura factura = facturaService.findById(id);
        return ResponseEntity.ok(facturaMapper.toDTO(factura));
    }

    // 🟠 Actualizar factura existente
    @PutMapping("/{id}")
    public ResponseEntity<FacturaDTO> update(@PathVariable Long id, @RequestBody FacturaCreateDTO dto) {
        Factura updated = facturaMapper.toEntity(dto);
        Factura saved = facturaService.update(id, updated);
        return ResponseEntity.ok(facturaMapper.toDTO(saved));
    }

    // 🔴 Eliminar factura
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        facturaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
