package ElBuenSabor.ProyectoFinal.Mappers;

import ElBuenSabor.ProyectoFinal.DTO.DetallePedidoCreateDTO;
import ElBuenSabor.ProyectoFinal.DTO.DetallePedidoDTO;
import ElBuenSabor.ProyectoFinal.Entities.ArticuloInsumo;
import ElBuenSabor.ProyectoFinal.Entities.ArticuloManufacturado;
import ElBuenSabor.ProyectoFinal.Entities.DetallePedido;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = {
                ArticuloInsumoMapper.class,
                ArticuloManufacturadoMapper.class
        },
        builder = @Builder(disableBuilder = true)
)
public interface DetallePedidoMapper {

    // Para mostrar el detalle con DTOs anidados
    @Mappings({
            @Mapping(source = "articuloInsumo.id", target = "articuloInsumoId"),
            @Mapping(source = "articuloManufacturado.id", target = "articuloManufacturadoId"),
            @Mapping(source = "baja", target = "estaDadoDeBaja")
    })
    DetallePedidoDTO toDTO(DetallePedido entity);

    // Para crear un detalle desde el cliente
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "estaDadoDeBaja", target = "baja"),
            @Mapping(target = "subTotal", ignore = true), // se calcula en el servicio
            @Mapping(source = "articuloInsumoId", target = "articuloInsumo", qualifiedByName = "mapArticuloInsumo"),
            @Mapping(source = "articuloManufacturadoId", target = "articuloManufacturado", qualifiedByName = "mapArticuloManufacturado"),
            @Mapping(target = "pedido", ignore = true) // se setea desde el Pedido en el service
    })
    DetallePedido toEntity(DetallePedidoCreateDTO dto);

    @Named("mapArticuloInsumo")
    default ArticuloInsumo mapArticuloInsumo(Long id) {
        if (id == null) return null;
        ArticuloInsumo a = new ArticuloInsumo();
        a.setId(id);
        return a;
    }

    @Named("mapArticuloManufacturado")
    default ArticuloManufacturado mapArticuloManufacturado(Long id) {
        if (id == null) return null;
        ArticuloManufacturado a = new ArticuloManufacturado();
        a.setId(id);
        return a;
    }
}