package ElBuenSabor.ProyectoFinal.Mappers;

import ElBuenSabor.ProyectoFinal.DTO.ArticuloManufacturadoDetalleDTO;
import ElBuenSabor.ProyectoFinal.Entities.ArticuloInsumo;
import ElBuenSabor.ProyectoFinal.Entities.ArticuloManufacturadoDetalle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Builder;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ArticuloManufacturadoDetalleMapper {

    @Mapping(source = "articuloInsumo.id", target = "articuloInsumoId")
    @Mapping(source = "baja", target = "estaDadoDeBaja")
    ArticuloManufacturadoDetalleDTO toDTO(ArticuloManufacturadoDetalle detalle);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "articuloInsumo", source = "articuloInsumoId")
    @Mapping(source = "estaDadoDeBaja", target = "baja")
    ArticuloManufacturadoDetalle toEntity(ArticuloManufacturadoDetalleDTO dto);

    // Método auxiliar para convertir ID a ArticuloInsumo (relación por ID)
    default ArticuloInsumo mapArticuloInsumoId(Long id) {
        if (id == null) return null;
        ArticuloInsumo insumo = new ArticuloInsumo();
        insumo.setId(id);
        return insumo;
    }
}