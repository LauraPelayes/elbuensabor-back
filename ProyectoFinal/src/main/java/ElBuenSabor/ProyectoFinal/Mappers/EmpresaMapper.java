package ElBuenSabor.ProyectoFinal.Mappers;

import ElBuenSabor.ProyectoFinal.DTO.EmpresaDTO;
import ElBuenSabor.ProyectoFinal.Entities.Empresa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SucursalMapper.class})
public interface EmpresaMapper {

    @Mapping(source = "baja", target = "estaDadoDeBaja")
    EmpresaDTO toDTO(Empresa empresa);

    @Mapping(source = "estaDadoDeBaja", target = "baja")
    Empresa toEntity(EmpresaDTO empresaDTO);
}