package ElBuenSabor.ProyectoFinal.Controllers;

import ElBuenSabor.ProyectoFinal.DTO.ArticuloInsumoFullDTO;
import ElBuenSabor.ProyectoFinal.DTO.ArticuloManufacturadoFullDTO;
// Necesitarás DTOs de respuesta si quieres devolver algo diferente a la entidad o al DTO de entrada.
// Por simplicidad, aquí a menudo devolveremos el mismo DTO de entrada/entidad o un DTO genérico.
// import ElBuenSabor.ProyectoFinal.DTO.ArticuloResponseDTO; // DTO genérico para respuestas de listados
import ElBuenSabor.ProyectoFinal.Entities.Articulo;
import ElBuenSabor.ProyectoFinal.Entities.ArticuloInsumo;
import ElBuenSabor.ProyectoFinal.Entities.ArticuloManufacturado;
import ElBuenSabor.ProyectoFinal.Service.ArticuloService;

// Importaciones para el mapeo a DTOs de respuesta
import ElBuenSabor.ProyectoFinal.DTO.CategoriaFullDTO;
import ElBuenSabor.ProyectoFinal.DTO.ImagenDTO;
import ElBuenSabor.ProyectoFinal.DTO.UnidadMedidaDTO;
import ElBuenSabor.ProyectoFinal.DTO.ArticuloManufacturadoDetalleDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/articulos")
@CrossOrigin(origins = "*") // Ajustar según necesidades de seguridad
public class ArticuloController {

    @Autowired
    private ArticuloService articuloService;

    // --- Endpoints para ArticuloInsumo ---

    @PostMapping("/insumos")
    public ResponseEntity<?> crearArticuloInsumo(@RequestBody ArticuloInsumoFullDTO insumoDTO) {
        try {
            ArticuloInsumo nuevoInsumo = articuloService.createArticuloInsumo(insumoDTO);
            return new ResponseEntity<>(convertToArticuloInsumoDTO(nuevoInsumo), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/insumos/{id}")
    public ResponseEntity<?> obtenerArticuloInsumoPorId(@PathVariable Long id) {
        try {
            // ArticuloService podría tener un findArticuloInsumoById que devuelva ArticuloInsumo
            // o usamos el genérico y casteamos/validamos.
            Optional<Articulo> articuloOptional = articuloService.findById(id); //
            if (articuloOptional.isPresent() && articuloOptional.get() instanceof ArticuloInsumo) {
                return ResponseEntity.ok(convertToArticuloInsumoDTO((ArticuloInsumo) articuloOptional.get()));
            } else {
                return new ResponseEntity<>("Artículo Insumo no encontrado.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/insumos/{id}")
    public ResponseEntity<?> actualizarArticuloInsumo(@PathVariable Long id, @RequestBody ArticuloInsumoFullDTO insumoDTO) {
        try {
            ArticuloInsumo insumoActualizado = articuloService.updateArticuloInsumo(id, insumoDTO);
            return ResponseEntity.ok(convertToArticuloInsumoDTO(insumoActualizado));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/insumos")
    public ResponseEntity<?> listarArticulosInsumo() {
        try {
            List<ArticuloInsumo> insumos = articuloService.findAllArticulosInsumo();
            List<ArticuloInsumoFullDTO> dtos = insumos.stream()
                    .map(this::convertToArticuloInsumoDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/insumos/stock-bajo")
    public ResponseEntity<?> listarArticulosInsumoConStockBajo(@RequestParam(defaultValue = "0") Double stockMinimoReferencia) {
        try {
            // Este método podría usar el stockMinimo de cada artículo o un valor de referencia.
            // El servicio ya tiene findArticulosInsumoByStockActualLessThanEqual que usa el stockMinimo de la entidad.
            // Si queremos usar un parámetro, el servicio debería tener un método que lo acepte.
            // Por ahora, usaré el método existente que compara con el stockMinimo propio del insumo.
            List<ArticuloInsumo> insumos = articuloService.findArticulosInsumoByStockActualLessThanEqual(stockMinimoReferencia); //
            List<ArticuloInsumoFullDTO> dtos = insumos.stream()
                    .map(this::convertToArticuloInsumoDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // --- Endpoints para ArticuloManufacturado ---

    @PostMapping("/manufacturados")
    public ResponseEntity<?> crearArticuloManufacturado(@RequestBody ArticuloManufacturadoFullDTO manufacturadoDTO) {
        try {
            ArticuloManufacturado nuevoManufacturado = articuloService.createArticuloManufacturado(manufacturadoDTO);
            return new ResponseEntity<>(convertToArticuloManufacturadoDTO(nuevoManufacturado), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/manufacturados/{id}")
    public ResponseEntity<?> obtenerArticuloManufacturadoPorId(@PathVariable Long id) {
        try {
            Optional<Articulo> articuloOptional = articuloService.findById(id); //
            if (articuloOptional.isPresent() && articuloOptional.get() instanceof ArticuloManufacturado) {
                return ResponseEntity.ok(convertToArticuloManufacturadoDTO((ArticuloManufacturado) articuloOptional.get()));
            } else {
                return new ResponseEntity<>("Artículo Manufacturado no encontrado.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/manufacturados/{id}")
    public ResponseEntity<?> actualizarArticuloManufacturado(@PathVariable Long id, @RequestBody ArticuloManufacturadoFullDTO manufacturadoDTO) {
        try {
            ArticuloManufacturado manufacturadoActualizado = articuloService.updateArticuloManufacturado(id, manufacturadoDTO);
            return ResponseEntity.ok(convertToArticuloManufacturadoDTO(manufacturadoActualizado));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/manufacturados")
    public ResponseEntity<?> listarArticulosManufacturado() {
        try {
            List<ArticuloManufacturado> manufacturados = articuloService.findAllArticulosManufacturados();
            List<ArticuloManufacturadoFullDTO> dtos = manufacturados.stream()
                    .map(this::convertToArticuloManufacturadoDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // --- Endpoints Generales para Articulos (Insumos y Manufacturados) ---

    // Endpoint para buscar artículos por denominación
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarArticulosPorDenominacion(@RequestParam String denominacion) {
        try {
            List<Articulo> articulos = articuloService.findByDenominacionContainingIgnoreCase(denominacion); //
            // Necesitamos una forma de convertir Articulo a un DTO genérico o específico
            List<Object> dtos = articulos.stream().map(articulo -> {
                if (articulo instanceof ArticuloInsumo) {
                    return convertToArticuloInsumoDTO((ArticuloInsumo) articulo);
                } else if (articulo instanceof ArticuloManufacturado) {
                    return convertToArticuloManufacturadoDTO((ArticuloManufacturado) articulo);
                }
                return null; // O un DTO base de Articulo
            }).collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para buscar artículos por categoría
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<?> buscarArticulosPorCategoria(@PathVariable Long categoriaId) {
        try {
            List<Articulo> articulos = articuloService.findByCategoriaId(categoriaId); //
            List<Object> dtos = articulos.stream().map(articulo -> {
                if (articulo instanceof ArticuloInsumo) {
                    return convertToArticuloInsumoDTO((ArticuloInsumo) articulo);
                } else if (articulo instanceof ArticuloManufacturado) {
                    return convertToArticuloManufacturadoDTO((ArticuloManufacturado) articulo);
                }
                return null;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para obtener todos los artículos (insumos y manufacturados)
    @GetMapping("")
    public ResponseEntity<?> listarTodosLosArticulos() {
        try {
            List<Articulo> articulos = articuloService.findAll(); //
            List<Object> dtos = articulos.stream().map(articulo -> {
                if (articulo instanceof ArticuloInsumo) {
                    return convertToArticuloInsumoDTO((ArticuloInsumo) articulo);
                } else if (articulo instanceof ArticuloManufacturado) {
                    return convertToArticuloManufacturadoDTO((ArticuloManufacturado) articulo);
                }
                // Considerar un ArticuloBaseDTO si no es ninguno de los dos (aunque no debería pasar con JOINED)
                return null;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Endpoint para eliminar un artículo (Insumo o Manufacturado)
    // Podría requerir lógica adicional en el servicio para verificar dependencias
    // (ej. si un insumo es parte de un manufacturado, si un manufacturado está en una promoción o pedido)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarArticulo(@PathVariable Long id) {
        try {
            // Antes de borrar, el servicio podría verificar si el artículo es Insumo o Manufacturado
            // y aplicar reglas específicas (ej. no borrar insumo si está en uso por un manufacturado).
            // La implementación actual de BaseService.delete(id) simplemente lo borrará.
            boolean eliminado = articuloService.delete(id); //
            if (eliminado) {
                return ResponseEntity.ok("Artículo eliminado correctamente.");
            } else {
                // Esto no debería pasar si findById antes de delete lanza excepción si no existe.
                // Pero si delete devuelve false por no encontrarlo:
                return new ResponseEntity<>("Artículo no encontrado para eliminar.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // --- Métodos Helper para convertir a DTOs de Respuesta ---

    private ArticuloInsumoFullDTO convertToArticuloInsumoDTO(ArticuloInsumo insumo) {
        if (insumo == null) return null;
        ArticuloInsumoFullDTO dto = new ArticuloInsumoFullDTO();
        dto.setId(insumo.getId());
        dto.setDenominacion(insumo.getDenominacion());
        dto.setPrecioVenta(insumo.getPrecioVenta());
        dto.setPrecioCompra(insumo.getPrecioCompra());
        dto.setStockActual(insumo.getStockActual());
        dto.setStockMinimo(insumo.getStockMinimo());
        dto.setEsParaElaborar(insumo.getEsParaElaborar());

        if (insumo.getUnidadMedida() != null) {
            dto.setUnidadMedidaId(insumo.getUnidadMedida().getId());
            UnidadMedidaDTO umDto = new UnidadMedidaDTO();
            umDto.setId(insumo.getUnidadMedida().getId());
            umDto.setDenominacion(insumo.getUnidadMedida().getDenominacion());
            dto.setUnidadMedida(umDto);
        }
        if (insumo.getCategoria() != null) {
            dto.setCategoriaId(insumo.getCategoria().getId());
            CategoriaFullDTO catDto = new CategoriaFullDTO();
            catDto.setId(insumo.getCategoria().getId());
            catDto.setDenominacion(insumo.getCategoria().getDenominacion());
            // No cargar sub/padre categorías aquí para evitar complejidad
            dto.setCategoria(catDto);
        }
        if (insumo.getImagen() != null) {
            dto.setImagenId(insumo.getImagen().getId());
            ImagenDTO imgDto = new ImagenDTO();
            imgDto.setId(insumo.getImagen().getId());
            imgDto.setDenominacion(insumo.getImagen().getDenominacion());
            dto.setImagen(imgDto);
        }
        return dto;
    }

    private ArticuloManufacturadoFullDTO convertToArticuloManufacturadoDTO(ArticuloManufacturado manufacturado) {
        if (manufacturado == null) return null;
        ArticuloManufacturadoFullDTO dto = new ArticuloManufacturadoFullDTO();
        dto.setId(manufacturado.getId());
        dto.setDenominacion(manufacturado.getDenominacion());
        dto.setPrecioVenta(manufacturado.getPrecioVenta());
        dto.setDescripcion(manufacturado.getDescripcion());
        dto.setTiempoEstimadoMinutos(manufacturado.getTiempoEstimadoMinutos());
        dto.setPreparacion(manufacturado.getPreparacion());


        if (manufacturado.getCategoria() != null) {
            dto.setCategoriaId(manufacturado.getCategoria().getId());
            CategoriaFullDTO catDto = new CategoriaFullDTO();
            catDto.setId(manufacturado.getCategoria().getId());
            catDto.setDenominacion(manufacturado.getCategoria().getDenominacion());
            dto.setCategoria(catDto);
        }
        if (manufacturado.getImagen() != null) {
            dto.setImagenId(manufacturado.getImagen().getId());
            ImagenDTO imgDto = new ImagenDTO();
            imgDto.setId(manufacturado.getImagen().getId());
            imgDto.setDenominacion(manufacturado.getImagen().getDenominacion());
            dto.setImagen(imgDto);
        }

        if (manufacturado.getDetalles() != null) {
            Set<ArticuloManufacturadoDetalleDTO> detallesDTO = manufacturado.getDetalles().stream().map(detalle -> {
                ArticuloManufacturadoDetalleDTO detalleDTO = new ArticuloManufacturadoDetalleDTO();
                detalleDTO.setId(detalle.getId());
                detalleDTO.setCantidad(detalle.getCantidad());
                if (detalle.getArticuloInsumo() != null) {
                    detalleDTO.setArticuloInsumoId(detalle.getArticuloInsumo().getId());
                    // Podrías anidar un ArticuloInsumoSimpleDTO aquí si es necesario
                    ArticuloInsumoFullDTO insumoDetalleDto = new ArticuloInsumoFullDTO();
                    insumoDetalleDto.setId(detalle.getArticuloInsumo().getId());
                    insumoDetalleDto.setDenominacion(detalle.getArticuloInsumo().getDenominacion());
                    detalleDTO.setArticuloInsumo(insumoDetalleDto);
                }
                return detalleDTO;
            }).collect(Collectors.toSet());
            dto.setDetalles(detallesDTO);
        }
        return dto;
    }
}