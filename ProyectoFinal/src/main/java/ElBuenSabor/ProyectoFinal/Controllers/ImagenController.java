package ElBuenSabor.ProyectoFinal.Controllers;

import ElBuenSabor.ProyectoFinal.DTO.ImagenDTO;
import ElBuenSabor.ProyectoFinal.Entities.Imagen;
import ElBuenSabor.ProyectoFinal.Service.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/imagenes")
@CrossOrigin(origins = "*")
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @PostMapping("")
    public ResponseEntity<?> createImagen(@RequestBody ImagenDTO imagenDTO) {
        try {
            Imagen nuevaImagen = imagenService.createImagen(imagenDTO);
            return new ResponseEntity<>(convertToImagenDTO(nuevaImagen), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImagenById(@PathVariable Long id) {
        try {
            Optional<Imagen> imagenOptional = imagenService.findById(id);
            if (imagenOptional.isPresent()) {
                return ResponseEntity.ok(convertToImagenDTO(imagenOptional.get()));
            } else {
                return new ResponseEntity<>("Imagen no encontrada.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllImagenes() {
        try {
            List<Imagen> imagenes = imagenService.findAll();
            List<ImagenDTO> dtos = imagenes.stream()
                    .map(this::convertToImagenDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateImagen(@PathVariable Long id, @RequestBody ImagenDTO imagenDTO) {
        try {
            Imagen imagenActualizada = imagenService.updateImagen(id, imagenDTO);
            return ResponseEntity.ok(convertToImagenDTO(imagenActualizada));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImagen(@PathVariable Long id) {
        try {
            // MUY IMPORTANTE: Antes de borrar una imagen, el servicio DEBE verificar
            // si está siendo usada por algún Artículo, Promoción, Cliente, etc.
            // Si está en uso, no se debería permitir el borrado o se deberían desasociar.
            // Esta lógica es compleja y debe estar en el ImagenService.delete(id)
            // o en un método específico de borrado seguro.
            boolean eliminado = imagenService.delete(id);
            if (eliminado) {
                return ResponseEntity.ok("Imagen eliminada (si no estaba en uso).");
            } else {
                return new ResponseEntity<>("Imagen no encontrada.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private ImagenDTO convertToImagenDTO(Imagen imagen) {
        if (imagen == null) return null;
        ImagenDTO dto = new ImagenDTO();
        dto.setId(imagen.getId());
        dto.setDenominacion(imagen.getDenominacion());
        return dto;
    }
}