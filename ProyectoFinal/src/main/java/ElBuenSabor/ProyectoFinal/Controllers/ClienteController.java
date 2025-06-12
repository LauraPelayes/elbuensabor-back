package ElBuenSabor.ProyectoFinal.Controllers;

import ElBuenSabor.ProyectoFinal.DTO.*;
import ElBuenSabor.ProyectoFinal.DTO.ClienteDTO; // Asumiendo que creamos este DTO
import ElBuenSabor.ProyectoFinal.Entities.Cliente;
import ElBuenSabor.ProyectoFinal.Mappers.ClienteMapper;
import ElBuenSabor.ProyectoFinal.Service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    // GET all
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> getAll() {
        List<Cliente> clientes = clienteService.findAll();
        List<ClienteDTO> dtos = clientes.stream()
                .map(clienteMapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getById(@PathVariable Long id) {
        Cliente cliente = clienteService.findById(id);
        return ResponseEntity.ok(clienteMapper.toDTO(cliente));
    }

    // POST - Crear cliente
    @PostMapping
    public ResponseEntity<ClienteDTO> create(@RequestBody ClienteCreateDTO createDTO) {
        Cliente cliente = clienteMapper.toEntity(createDTO);
        Cliente saved = clienteService.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteMapper.toDTO(saved));
    }

    // PUT - Actualizar cliente
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> update(@PathVariable Long id, @RequestBody ClienteCreateDTO updateDTO) {
        Cliente cliente = clienteMapper.toEntity(updateDTO);
        Cliente updated = clienteService.update(id, cliente);
        return ResponseEntity.ok(clienteMapper.toDTO(updated));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
