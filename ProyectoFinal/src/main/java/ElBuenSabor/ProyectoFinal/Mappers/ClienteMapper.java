package ElBuenSabor.ProyectoFinal.Mappers;

import ElBuenSabor.ProyectoFinal.DTO.ClienteActualizacionDTO;
import ElBuenSabor.ProyectoFinal.DTO.ClienteRegistroDTO;
import ElBuenSabor.ProyectoFinal.DTO.ClienteResponseDTO;
import ElBuenSabor.ProyectoFinal.Entities.Cliente;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(
        componentModel = "spring",
        uses = {DomicilioMapper.class, ImagenMapper.class},
        builder = @Builder(disableBuilder = true)
)
public interface ClienteMapper {

    // 🔁 Entidad → DTO de respuesta
    @Mappings({
            @Mapping(source = "baja", target = "estaDadoDeBaja"),
            @Mapping(source = "usuario.username", target = "username"),
            @Mapping(source = "usuario.auth0Id", target = "auth0Id"),
            @Mapping(source = "imagen", target = "imagen")
    })
    ClienteResponseDTO toClienteResponseDTO(Cliente cliente);

    // 🔁 DTO de registro → Entidad
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "estaDadoDeBaja", target = "baja"),
            @Mapping(target = "pedidos", ignore = true),
            @Mapping(target = "usuario", ignore = true),
            @Mapping(target = "domicilios", ignore = true), // Se maneja en servicio
            @Mapping(target = "imagen.id", source = "imagenId") // Si vas a setear la imagen
    })
    Cliente toEntity(ClienteRegistroDTO clienteRegistroDTO);

    // 🔁 DTO de actualización → Entidad
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "estaDadoDeBaja", target = "baja"),
            @Mapping(target = "pedidos", ignore = true),
            @Mapping(target = "usuario", ignore = true),
            @Mapping(target = "domicilios", ignore = true),
            @Mapping(target = "imagen.id", source = "imagenId")
    })
    Cliente toEntity(ClienteActualizacionDTO clienteActualizacionDTO);
}