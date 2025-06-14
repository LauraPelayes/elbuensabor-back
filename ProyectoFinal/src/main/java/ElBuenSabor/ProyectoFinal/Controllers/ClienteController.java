package ElBuenSabor.ProyectoFinal.Controllers;

import ElBuenSabor.ProyectoFinal.DTO.ClienteCreateDTO;
import ElBuenSabor.ProyectoFinal.DTO.ClienteDTO;
import ElBuenSabor.ProyectoFinal.Entities.Cliente;
import ElBuenSabor.ProyectoFinal.Mappers.ClienteMapper;
import ElBuenSabor.ProyectoFinal.Service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes") // Define la URL base para este controlador
// ClienteController ahora extiende BaseController
public class ClienteController extends BaseController<Cliente, Long> {

    private final ClienteMapper clienteMapper;

    // El constructor inyecta el servicio específico de Cliente y el mapper
    public ClienteController(
            ClienteService clienteService, // Servicio específico
            ClienteMapper clienteMapper) {
        super(clienteService); // Pasa el servicio al constructor del BaseController
        this.clienteMapper = clienteMapper;
    }

    // Sobrescribir getAll para devolver DTOs y manejar excepciones
    @GetMapping
    @Override // Sobrescribe el getAll del BaseController
    public ResponseEntity<?> getAll() {
        try {
            List<Cliente> clientes = baseService.findAll(); // Llama al findAll del padre
            List<ClienteDTO> dtos = clientes.stream()
                    .map(clienteMapper::toDTO)
                    .toList();
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    // Sobrescribir getOne para devolver un DTO y manejar excepciones
    @GetMapping("/{id}")
    @Override // Sobrescribe el getOne del BaseController
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        try {
            Cliente cliente = baseService.findById(id); // Llama al findById del padre
            return ResponseEntity.ok(clienteMapper.toDTO(cliente));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    // Sobrescribir create para aceptar un DTO de entrada, mapear y manejar excepciones
    @PostMapping(consumes = "application/json")
    // @Override // <<--- Quitar @Override aquí, ya que la firma del método es diferente (recibe DTO)
    public ResponseEntity<?> create(@RequestBody ClienteCreateDTO createDTO) {
        try {
            Cliente cliente = clienteMapper.toEntity(createDTO);
            cliente.setBaja(false); // Por defecto, un nuevo cliente está activo

            // Aquí deberías establecer las relaciones si el DTO solo proporciona IDs
            // Por ejemplo, si ClienteCreateDTO tiene usuarioId, imagenId, domicilioIds
            // cliente.setUsuario(usuarioService.findById(createDTO.getUsuarioId())); // Necesitarías inyectar UsuarioService
            // cliente.setImagen(imagenService.findById(createDTO.getImagenId()));   // Necesitarías inyectar ImagenService
            // cliente.setDomicilios(domicilioService.findAllById(createDTO.getDomicilioIds())); // Necesitarías inyectar DomicilioService

            Cliente saved = baseService.save(cliente); // Llama al save del padre
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteMapper.toDTO(saved)); // Convierte a DTO para la respuesta
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    // Sobrescribir update para aceptar un DTO de entrada, mapear y manejar excepciones
    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ClienteCreateDTO updateDTO) {
        try {
            Cliente cliente = clienteMapper.toEntity(updateDTO); // Esto crea una nueva entidad, es mejor buscar la existente

            // Es mejor obtener la entidad existente y actualizar sus propiedades
            Cliente existing = baseService.findById(id); // Obtiene el cliente existente

            existing.setNombre(cliente.getNombre());
            existing.setApellido(cliente.getApellido());
            existing.setTelefono(cliente.getTelefono());
            existing.setEmail(cliente.getEmail());
            // Si el DTO de actualización incluye la contraseña, MANEJAR CON MUCHO CUIDADO (ej. BCryptPasswordEncoder)
            // existing.setPassword(cliente.getPassword());
            existing.setFechaNacimiento(cliente.getFechaNacimiento());
            // existing.setBaja(cliente.getBaja()); // La baja se maneja con toggleBaja o el update del servicio

            // Actualizar relaciones (similar al create, necesitarías servicios inyectados)
            existing.setImagen(cliente.getImagen());
            existing.setUsuario(cliente.getUsuario());
            // Para colecciones como Domicilios, es crucial sincronizar.
            // Si DomicilioCreateDTO solo envía IDs, necesitarías inyectar DomicilioService aquí.
            if (cliente.getDomicilios() != null) {
                existing.getDomicilios().clear();
                existing.getDomicilios().addAll(cliente.getDomicilios());
                // Asegúrate de setear la relación inversa si es bidireccional y Cascade no lo hace.
                existing.getDomicilios().forEach(dom -> dom.setCliente(existing));
            }


            Cliente updated = baseService.update(id, existing); // Llama al update del padre con la entidad EXISTENTE
            return ResponseEntity.ok(clienteMapper.toDTO(updated)); // Convierte a DTO para la respuesta
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    // Los métodos DELETE, ACTIVATE, DEACTIVATE pueden heredarse directamente de BaseController
    // si la lógica de borrado/activación/desactivación ya implementada en BaseController
    // es suficiente y no necesitas una respuesta con DTOs específicos.
    // @DeleteMapping("/{id}") ya está cubierto por BaseController
    // @PatchMapping("/{id}/activate") ya está cubierto por BaseController
    // @PatchMapping("/{id}/deactivate") ya está cubierto por BaseController
}