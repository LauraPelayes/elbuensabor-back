package ElBuenSabor.ProyectoFinal.Controllers;

import ElBuenSabor.ProyectoFinal.DTO.ArticuloManufacturadoCreateDTO;
import ElBuenSabor.ProyectoFinal.DTO.ArticuloManufacturadoDTO;
import ElBuenSabor.ProyectoFinal.Entities.ArticuloManufacturado;
import ElBuenSabor.ProyectoFinal.Mappers.ArticuloManufacturadoMapper;
import ElBuenSabor.ProyectoFinal.Repositories.CategoriaRepository;
import ElBuenSabor.ProyectoFinal.Repositories.ImagenRepository;
import ElBuenSabor.ProyectoFinal.Repositories.UnidadMedidaRepository;
import ElBuenSabor.ProyectoFinal.Service.ArticuloManufacturadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articuloManufacturado")
@RequiredArgsConstructor
public class ArticuloManufacturadoController {

    private final ArticuloManufacturadoService service;
    private final ArticuloManufacturadoMapper mapper;
    private final CategoriaRepository categoriaRepository;
    private final UnidadMedidaRepository unidadMedidaRepository;
    private final ImagenRepository imagenRepository;

    @GetMapping("/manufacturados")
    public ResponseEntity<List<ArticuloManufacturadoDTO>> getAll() {
        return ResponseEntity.ok(
                service.findAll().stream()
                        .map(mapper::toDTO)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticuloManufacturadoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDTO(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ArticuloManufacturadoDTO> create(@RequestBody ArticuloManufacturadoCreateDTO dto) {
        ArticuloManufacturado entity = mapper.toEntity(dto);

        entity.setCategoria(categoriaRepository.findById(dto.getCategoriaId()).orElse(null));
        entity.setUnidadMedida(unidadMedidaRepository.findById(dto.getUnidadMedidaId()).orElse(null));
        entity.setImagen(imagenRepository.findById(dto.getImagenId()).orElse(null));

        ArticuloManufacturado saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticuloManufacturadoDTO> update(@PathVariable Long id, @RequestBody ArticuloManufacturadoCreateDTO dto) {

        ArticuloManufacturado entity = mapper.toEntity(dto);

        entity.setCategoria(categoriaRepository.findById(dto.getCategoriaId()).orElse(null));
        entity.setUnidadMedida(unidadMedidaRepository.findById(dto.getUnidadMedidaId()).orElse(null));
        entity.setImagen(imagenRepository.findById(dto.getImagenId()).orElse(null));

        ArticuloManufacturado updated = service.update(id, entity);
        return ResponseEntity.ok(mapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
