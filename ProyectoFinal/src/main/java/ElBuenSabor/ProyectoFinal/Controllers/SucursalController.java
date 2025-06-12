package ElBuenSabor.ProyectoFinal.Controllers;

import ElBuenSabor.ProyectoFinal.DTO.*;
import ElBuenSabor.ProyectoFinal.Entities.Categoria;
import ElBuenSabor.ProyectoFinal.Entities.Promocion;
import ElBuenSabor.ProyectoFinal.Entities.Sucursal;
import ElBuenSabor.ProyectoFinal.Exceptions.ResourceNotFoundException;
import ElBuenSabor.ProyectoFinal.Mappers.SucursalMapper;
import ElBuenSabor.ProyectoFinal.Repositories.CategoriaRepository;
import ElBuenSabor.ProyectoFinal.Repositories.DomicilioRepository;
import ElBuenSabor.ProyectoFinal.Repositories.EmpresaRepository;
import ElBuenSabor.ProyectoFinal.Repositories.PromocionRepository;
import ElBuenSabor.ProyectoFinal.Service.SucursalService;
import ElBuenSabor.ProyectoFinal.Service.CategoriaService; // Para obtener categorías
import ElBuenSabor.ProyectoFinal.Service.PromocionService; // Para obtener promociones
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDate; // Para promociones activas
import java.time.LocalTime; // Para promociones activas


@RestController
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
public class SucursalController {

    private final SucursalService sucursalService;
    private final SucursalMapper sucursalMapper;

    private final DomicilioRepository domicilioRepository;
    private final EmpresaRepository empresaRepository;
    private final CategoriaRepository categoriaRepository;
    private final PromocionRepository promocionRepository;

    // 🟢 Crear sucursal
    @PostMapping
    public ResponseEntity<SucursalDTO> create(@RequestBody SucursalCreateDTO dto) {
        Sucursal sucursal = sucursalMapper.toEntity(dto);

        sucursal.setDomicilio(domicilioRepository.findById(dto.getDomicilioId())
                .orElseThrow(() -> new ResourceNotFoundException("Domicilio no encontrado")));

        sucursal.setEmpresa(empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada")));

        if (dto.getCategoriaIds() != null) {
            List<Categoria> categorias = categoriaRepository.findAllById(dto.getCategoriaIds());
            sucursal.setCategorias(categorias);
        }

        if (dto.getPromocionIds() != null) {
            List<Promocion> promociones = promocionRepository.findAllById(dto.getPromocionIds());
            sucursal.setPromociones(promociones);
        }

        Sucursal saved = sucursalService.save(sucursal);
        return ResponseEntity.status(HttpStatus.CREATED).body(sucursalMapper.toDTO(saved));
    }

    // 🔵 Obtener todas las sucursales
    @GetMapping
    public ResponseEntity<List<SucursalDTO>> getAll() {
        List<Sucursal> sucursales = sucursalService.findAll();
        return ResponseEntity.ok(sucursalMapper.toDTOList(sucursales));
    }

    // 🟣 Obtener sucursal por ID
    @GetMapping("/{id}")
    public ResponseEntity<SucursalDTO> getById(@PathVariable Long id) {
        Sucursal sucursal = sucursalService.findById(id);
        return ResponseEntity.ok(sucursalMapper.toDTO(sucursal));
    }

    // 🟠 Actualizar sucursal
    @PutMapping("/{id}")
    public ResponseEntity<SucursalDTO> update(@PathVariable Long id, @RequestBody SucursalCreateDTO dto) {
        Sucursal sucursal = sucursalMapper.toEntity(dto);

        sucursal.setDomicilio(domicilioRepository.findById(dto.getDomicilioId())
                .orElseThrow(() -> new ResourceNotFoundException("Domicilio no encontrado")));

        sucursal.setEmpresa(empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada")));

        if (dto.getCategoriaIds() != null) {
            List<Categoria> categorias = categoriaRepository.findAllById(dto.getCategoriaIds());
            sucursal.setCategorias(categorias);
        }

        if (dto.getPromocionIds() != null) {
            List<Promocion> promociones = promocionRepository.findAllById(dto.getPromocionIds());
            sucursal.setPromociones(promociones);
        }

        Sucursal updated = sucursalService.update(id, sucursal);
        return ResponseEntity.ok(sucursalMapper.toDTO(updated));
    }

    // 🔴 Eliminar sucursal
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sucursalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
