package ElBuenSabor.ProyectoFinal.Controllers;

import ElBuenSabor.ProyectoFinal.DTO.ArticuloInsumoDTO;
import ElBuenSabor.ProyectoFinal.DTO.ArticuloManufacturadoDTO;
import ElBuenSabor.ProyectoFinal.Entities.Articulo;
import ElBuenSabor.ProyectoFinal.Entities.ArticuloInsumo;
import ElBuenSabor.ProyectoFinal.Entities.ArticuloManufacturado;
import ElBuenSabor.ProyectoFinal.Mappers.ArticuloMapper;
import ElBuenSabor.ProyectoFinal.Service.ArticuloService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/articulos")
@CrossOrigin(origins = "*")
public class ArticuloController {

    private final ArticuloService articuloService;
    private final ArticuloMapper articuloMapper;

    public ArticuloController(ArticuloService articuloService, ArticuloMapper articuloMapper) {
        this.articuloService = articuloService;
        this.articuloMapper = articuloMapper;
    }

    // --- Endpoints para ArticuloInsumo ---

    @PostMapping("/insumos")
    public ResponseEntity<ArticuloInsumoDTO> crearArticuloInsumo(@RequestBody ArticuloInsumoDTO dto) throws Exception {
        ArticuloInsumo insumo = articuloMapper.toInsumoEntity(dto);
        ArticuloInsumo nuevoInsumo = articuloService.createArticuloInsumo(insumo);
        return ResponseEntity.status(HttpStatus.CREATED).body(articuloMapper.toInsumoDTO(nuevoInsumo));
    }

    @PutMapping("/insumos/{id}")
    public ResponseEntity<ArticuloInsumoDTO> actualizarArticuloInsumo(@PathVariable Long id, @RequestBody ArticuloInsumoDTO dto) throws Exception {
        ArticuloInsumo insumo = articuloMapper.toInsumoEntity(dto);
        ArticuloInsumo actualizado = articuloService.updateArticuloInsumo(id, insumo);
        return ResponseEntity.ok(articuloMapper.toInsumoDTO(actualizado));
    }

    @GetMapping("/insumos/{id}")
    public ResponseEntity<ArticuloInsumoDTO> obtenerInsumoPorId(@PathVariable Long id) throws Exception {
        Articulo articulo = articuloService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artículo no encontrado"));
        if (!(articulo instanceof ArticuloInsumo insumo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El artículo no es un insumo");
        }
        return ResponseEntity.ok(articuloMapper.toInsumoDTO(insumo));
    }

    @GetMapping("/insumos")
    public ResponseEntity<List<ArticuloInsumoDTO>> listarArticulosInsumo() throws Exception {
        List<ArticuloInsumo> insumos = articuloService.findAllArticulosInsumo();
        List<ArticuloInsumoDTO> dtos = insumos.stream().map(articuloMapper::toInsumoDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // --- Endpoints para ArticuloManufacturado ---

    @PostMapping("/manufacturados")
    public ResponseEntity<ArticuloManufacturadoDTO> crearArticuloManufacturado(@RequestBody ArticuloManufacturadoDTO dto) throws Exception {
        ArticuloManufacturado manufacturado = articuloMapper.toManufacturadoEntity(dto);
        ArticuloManufacturado nuevo = articuloService.createArticuloManufacturado(manufacturado);
        return ResponseEntity.status(HttpStatus.CREATED).body(articuloMapper.toManufacturadoDTO(nuevo));
    }

    @PutMapping("/manufacturados/{id}")
    public ResponseEntity<ArticuloManufacturadoDTO> actualizarArticuloManufacturado(@PathVariable Long id, @RequestBody ArticuloManufacturadoDTO dto) throws Exception {
        ArticuloManufacturado manufacturado = articuloMapper.toManufacturadoEntity(dto);
        ArticuloManufacturado actualizado = articuloService.updateArticuloManufacturado(id, manufacturado);
        return ResponseEntity.ok(articuloMapper.toManufacturadoDTO(actualizado));
    }

    @GetMapping("/manufacturados/{id}")
    public ResponseEntity<ArticuloManufacturadoDTO> obtenerManufacturadoPorId(@PathVariable Long id) throws Exception {
        Articulo articulo = articuloService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artículo no encontrado"));
        if (!(articulo instanceof ArticuloManufacturado manufacturado)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El artículo no es manufacturado");
        }
        return ResponseEntity.ok(articuloMapper.toManufacturadoDTO(manufacturado));
    }

    @GetMapping("/manufacturados")
    public ResponseEntity<List<ArticuloManufacturadoDTO>> listarArticulosManufacturados() throws Exception {
        List<ArticuloManufacturado> manufacturados = articuloService.findAllArticulosManufacturados();
        List<ArticuloManufacturadoDTO> dtos = manufacturados.stream().map(articuloMapper::toManufacturadoDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // --- Endpoints Generales (GET y DELETE) ---

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws Exception {
        articuloService.delete(id);
        return ResponseEntity.noContent().build();
    }
}