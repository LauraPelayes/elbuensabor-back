package ElBuenSabor.ProyectoFinal.Controllers;

import ElBuenSabor.ProyectoFinal.DTO.PaisDTO;
import ElBuenSabor.ProyectoFinal.Entities.Pais;
import ElBuenSabor.ProyectoFinal.Service.PaisService;
import ElBuenSabor.ProyectoFinal.Service.ProvinciaService; // Para listar provincias de un país
import ElBuenSabor.ProyectoFinal.DTO.ProvinciaDTO; // Para la respuesta de provincias

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/paises")
@CrossOrigin(origins = "*")
public class PaisController {

    @Autowired
    private PaisService paisService;

    @Autowired
    private ProvinciaService provinciaService; // Para el endpoint de provincias por país

    @PostMapping("")
    public ResponseEntity<?> createPais(@RequestBody PaisDTO paisDTO) {
        try {
            Pais nuevoPais = paisService.createPais(paisDTO);
            // El servicio ya devuelve la entidad, podemos convertirla a DTO si es diferente
            // o si el DTO tiene más/menos campos que la entidad para la respuesta.
            // Por simplicidad, si PaisDTO es igual a la entidad para respuesta:
            return new ResponseEntity<>(convertToPaisDTO(nuevoPais), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaisById(@PathVariable Long id) {
        try {
            Optional<Pais> paisOptional = paisService.findById(id);
            if (paisOptional.isPresent()) {
                return ResponseEntity.ok(convertToPaisDTO(paisOptional.get()));
            } else {
                return new ResponseEntity<>("País no encontrado.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllPaises() {
        try {
            List<Pais> paises = paisService.findAll();
            List<PaisDTO> dtos = paises.stream()
                    .map(this::convertToPaisDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> getPaisByNombre(@RequestParam String nombre) {
        try {
            Pais pais = paisService.findByNombre(nombre);
            if (pais != null) {
                return ResponseEntity.ok(convertToPaisDTO(pais));
            } else {
                return new ResponseEntity<>("País no encontrado con el nombre: " + nombre, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePais(@PathVariable Long id, @RequestBody PaisDTO paisDTO) {
        try {
            Pais paisActualizado = paisService.updatePais(id, paisDTO);
            return ResponseEntity.ok(convertToPaisDTO(paisActualizado));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePais(@PathVariable Long id) {
        try {
            // Considerar no eliminar si tiene provincias asociadas.
            // Esta lógica debería estar en el PaisService.delete o un método específico.
            boolean eliminado = paisService.delete(id);
            if (eliminado) {
                return ResponseEntity.ok("País eliminado correctamente.");
            } else {
                return new ResponseEntity<>("País no encontrado.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint para obtener las provincias de un país específico
    @GetMapping("/{id}/provincias")
    public ResponseEntity<?> obtenerProvinciasPorPais(@PathVariable Long id) {
        try {
            // Verificar si el país existe
            if (!paisService.existsById(id)) {
                return new ResponseEntity<>("País no encontrado.", HttpStatus.NOT_FOUND);
            }
            List<ProvinciaDTO> dtos = provinciaService.findByPaisId(id);
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private PaisDTO convertToPaisDTO(Pais pais) {
        if (pais == null) return null;
        PaisDTO dto = new PaisDTO();
        dto.setId(pais.getId());
        dto.setNombre(pais.getNombre());
        // No tiene más relaciones directas para mostrar en un DTO simple
        return dto;
    }
}