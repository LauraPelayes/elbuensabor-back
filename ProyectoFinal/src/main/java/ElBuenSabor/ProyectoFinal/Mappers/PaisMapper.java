package ElBuenSabor.ProyectoFinal.Mappers;

import ElBuenSabor.ProyectoFinal.DTO.PaisDTO;
import ElBuenSabor.ProyectoFinal.Entities.Pais;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface PaisMapper {

    @Mapping(source = "baja", target = "estaDadoDeBaja")
    PaisDTO toDTO(Pais pais);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "provincias", ignore = true)
    @Mapping(source = "estaDadoDeBaja", target = "baja")
    Pais toEntity(PaisDTO paisDTO);
}