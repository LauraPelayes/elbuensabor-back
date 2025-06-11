package ElBuenSabor.ProyectoFinal.Mappers;

import ElBuenSabor.ProyectoFinal.DTO.ArticuloManufacturadoDTO;
import ElBuenSabor.ProyectoFinal.Entities.ArticuloManufacturado;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(
        componentModel = "spring",
        uses = {
                CategoriaMapper.class,
                ImagenMapper.class,
                ArticuloManufacturadoDetalleMapper.class
        },
        builder = @Builder(disableBuilder = true)
)
public interface ArticuloManufacturadoMapper {

    @Mappings({
            @Mapping(source = "categoria.id", target = "categoriaId"),
            @Mapping(source = "imagen.id", target = "imagenId"),
            @Mapping(source = "baja", target = "estaDadoDeBaja")
    })
    ArticuloManufacturadoDTO toDTO(ArticuloManufacturado entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "estaDadoDeBaja", target = "baja"),
            @Mapping(target = "categoria", ignore = true),
            @Mapping(target = "imagen", ignore = true),
            @Mapping(target = "detalles", ignore = true) // se setea desde el servicio si es necesario
    })
    ArticuloManufacturado toEntity(ArticuloManufacturadoDTO dto);
}