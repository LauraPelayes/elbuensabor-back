package ElBuenSabor.ProyectoFinal.Mappers;

import ElBuenSabor.ProyectoFinal.DTO.LocalidadCreateUpdateDTO;
import ElBuenSabor.ProyectoFinal.DTO.LocalidadDTO;
import ElBuenSabor.ProyectoFinal.Entities.Localidad;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface LocalidadMapper {

    @Mapping(source = "provincia.id", target = "provinciaId")
    @Mapping(source = "baja", target = "estaDadoDeBaja")
    LocalidadDTO toDTO(Localidad localidad);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "estaDadoDeBaja", target = "baja")
    @Mapping(target = "provincia", ignore = true) // Se maneja en el servicio
    Localidad toEntity(LocalidadCreateUpdateDTO localidadCreateUpdateDTO);
}