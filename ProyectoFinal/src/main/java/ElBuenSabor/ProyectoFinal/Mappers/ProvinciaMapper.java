package ElBuenSabor.ProyectoFinal.Mappers;

import ElBuenSabor.ProyectoFinal.DTO.ProvinciaCreateUpdateDTO;
import ElBuenSabor.ProyectoFinal.DTO.ProvinciaDTO;
import ElBuenSabor.ProyectoFinal.Entities.Provincia;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ProvinciaMapper {

    @Mappings({
            @Mapping(source = "pais.id", target = "paisId"),
            @Mapping(source = "pais", target = "pais"), // mapea al objeto completo
            @Mapping(source = "baja", target = "estaDadoDeBaja")
    })
    ProvinciaDTO toDTO(Provincia provincia);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "estaDadoDeBaja", target = "baja"),
            @Mapping(target = "localidades", ignore = true),
            @Mapping(target = "pais", ignore = true) // Lo cargás desde el servicio con el paisId
    })
    Provincia toEntity(ProvinciaCreateUpdateDTO dto);
}