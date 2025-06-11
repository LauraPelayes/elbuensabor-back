package ElBuenSabor.ProyectoFinal.Mappers;

import ElBuenSabor.ProyectoFinal.DTO.DomicilioCreateUpdateDTO;
import ElBuenSabor.ProyectoFinal.DTO.DomicilioDTO;
import ElBuenSabor.ProyectoFinal.Entities.Domicilio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Builder;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {LocalidadMapper.class}, builder = @Builder(disableBuilder = true))
public interface DomicilioMapper {

    @Mappings({
            @Mapping(source = "localidad.id", target = "localidadId"),
            @Mapping(source = "localidad", target = "localidad"),
            @Mapping(source = "baja", target = "estaDadoDeBaja")
    })
    DomicilioDTO toDTO(Domicilio domicilio);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "estaDadoDeBaja", target = "baja"),
            @Mapping(target = "clientes", ignore = true),
            @Mapping(target = "pedidos", ignore = true),
            @Mapping(target = "localidad", ignore = true)
    })
    Domicilio toEntity(DomicilioCreateUpdateDTO dto);
}