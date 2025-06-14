package ElBuenSabor.ProyectoFinal.Controllers;

import ElBuenSabor.ProyectoFinal.DTO.*;
import ElBuenSabor.ProyectoFinal.Entities.ArticuloManufacturado;
import ElBuenSabor.ProyectoFinal.Entities.Promocion;
import ElBuenSabor.ProyectoFinal.Entities.Sucursal;
import ElBuenSabor.ProyectoFinal.Exceptions.ResourceNotFoundException;
import ElBuenSabor.ProyectoFinal.Mappers.PromocionMapper;
import ElBuenSabor.ProyectoFinal.Repositories.ArticuloManufacturadoRepository;
import ElBuenSabor.ProyectoFinal.Repositories.ImagenRepository;
import ElBuenSabor.ProyectoFinal.Repositories.SucursalRepository;
import ElBuenSabor.ProyectoFinal.Service.PromocionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Importaciones para el mapeo a DTOs de respuesta (si fueran diferentes o más complejos)


@RestController
@RequestMapping("/api/promociones")
@RequiredArgsConstructor
public class PromocionController {

    private final PromocionService promocionService;
    private final PromocionMapper promocionMapper;

    private final ImagenRepository imagenRepository;
    private final ArticuloManufacturadoRepository articuloRepo;
    private final SucursalRepository sucursalRepository;

    // Crear promoción
    @PostMapping
    public ResponseEntity<PromocionDTO> create(@RequestBody PromocionCreateDTO dto) {
        Promocion promocion = promocionMapper.toEntity(dto);

        if (dto.getImagenId() != null) {
            promocion.setImagen(imagenRepository.findById(dto.getImagenId())
                    .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada")));
        }

        if (dto.getArticuloManufacturadoIds() != null) {
            List<ArticuloManufacturado> articulos = articuloRepo.findAllById(dto.getArticuloManufacturadoIds());
            promocion.setArticulosManufacturados(articulos);
        }

        if (dto.getSucursalIds() != null) {
            List<Sucursal> sucursales = sucursalRepository.findAllById(dto.getSucursalIds());
            promocion.setSucursales(sucursales);
        }

        Promocion saved = promocionService.save(promocion);
        return ResponseEntity.status(HttpStatus.CREATED).body(promocionMapper.toDTO(saved));
    }

    // Obtener todas
    @GetMapping
    public ResponseEntity<List<PromocionDTO>> getAll() {
        List<Promocion> promociones = promocionService.findAll();
        return ResponseEntity.ok(promocionMapper.toDTOList(promociones));
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<PromocionDTO> getById(@PathVariable Long id) {
        Promocion promocion = promocionService.findById(id);
        return ResponseEntity.ok(promocionMapper.toDTO(promocion));
    }

    // Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<PromocionDTO> update(@PathVariable Long id, @RequestBody PromocionCreateDTO dto) {
        Promocion promocion = promocionMapper.toEntity(dto);

        if (dto.getImagenId() != null) {
            promocion.setImagen(imagenRepository.findById(dto.getImagenId())
                    .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada")));
        }

        if (dto.getArticuloManufacturadoIds() != null) {
            List<ArticuloManufacturado> articulos = articuloRepo.findAllById(dto.getArticuloManufacturadoIds());
            promocion.setArticulosManufacturados(articulos);
        }

        if (dto.getSucursalIds() != null) {
            List<Sucursal> sucursales = sucursalRepository.findAllById(dto.getSucursalIds());
            promocion.setSucursales(sucursales);
        }

        Promocion updated = promocionService.update(id, promocion);
        return ResponseEntity.ok(promocionMapper.toDTO(updated));
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        promocionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
