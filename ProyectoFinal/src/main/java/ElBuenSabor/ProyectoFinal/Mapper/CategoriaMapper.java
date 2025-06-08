package ElBuenSabor.ProyectoFinal.Mapper;

import ElBuenSabor.ProyectoFinal.DTO.CategoriaFullDTO;
import ElBuenSabor.ProyectoFinal.DTO.CategoriaShortDTO;
import ElBuenSabor.ProyectoFinal.Entities.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    CategoriaFullDTO categoriaToCategoriaFullDto(Categoria categoria);
    Categoria categoriaFullDtoToCategoria(CategoriaFullDTO categoriaFullDto);

    CategoriaShortDTO categoriaToCategoriaShortDto(Categoria categoria);

}
