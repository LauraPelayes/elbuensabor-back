package ElBuenSabor.ProyectoFinal.Mappers;

import ElBuenSabor.ProyectoFinal.DTO.UnidadMedidaDTO;
import ElBuenSabor.ProyectoFinal.Entities.UnidadMedida;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Builder;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UnidadMedidaMapper {

    // Entidad → DTO
    @Mapping(source = "baja", target = "estaDadoDeBaja")
    UnidadMedidaDTO toDTO(UnidadMedida unidadMedida);

    // DTO → Entidad
    @Mapping(target = "id", source = "id")
    @Mapping(source = "estaDadoDeBaja", target = "baja")
    UnidadMedida toEntity(UnidadMedidaDTO dto);

    // Utilidad para relacionar por ID
    default UnidadMedida fromId(Long id) {
        if (id == null) return null;
        UnidadMedida unidad = new UnidadMedida();
        unidad.setId(id);
        return unidad;
    }
}