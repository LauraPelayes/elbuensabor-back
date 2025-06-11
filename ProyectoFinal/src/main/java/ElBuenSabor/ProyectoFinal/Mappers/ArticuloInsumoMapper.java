package ElBuenSabor.ProyectoFinal.Mappers;

import ElBuenSabor.ProyectoFinal.DTO.ArticuloInsumoDTO;
import ElBuenSabor.ProyectoFinal.Entities.ArticuloInsumo;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(
        componentModel = "spring",
        uses = {
                UnidadMedidaMapper.class,
                CategoriaMapper.class,
                ImagenMapper.class
        },
        builder = @Builder(disableBuilder = true)
)
public interface ArticuloInsumoMapper {

    @Mappings({
            @Mapping(source = "unidadMedida.id", target = "unidadMedidaId"),
            @Mapping(source = "categoria.id", target = "categoriaId"),
            @Mapping(source = "imagen.id", target = "imagenId"),
            @Mapping(source = "baja", target = "estaDadoDeBaja")
    })
    ArticuloInsumoDTO toDTO(ArticuloInsumo entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "estaDadoDeBaja", target = "baja"),
            @Mapping(target = "unidadMedida", ignore = true),
            @Mapping(target = "categoria", ignore = true),
            @Mapping(target = "imagen", ignore = true),
    })
    ArticuloInsumo toEntity(ArticuloInsumoDTO dto);
}