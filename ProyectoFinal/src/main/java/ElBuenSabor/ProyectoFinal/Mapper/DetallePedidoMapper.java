package ElBuenSabor.ProyectoFinal.Mapper;

import ElBuenSabor.ProyectoFinal.DTO.DetallePedidoFullDTO;
import ElBuenSabor.ProyectoFinal.DTO.DetallePedidoShortDTO;
import ElBuenSabor.ProyectoFinal.Entities.DetallePedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DetallePedidoMapper {

    DetallePedidoFullDTO detallePedidoToDetallePedidoFullDTO(DetallePedido detallePedido);
    DetallePedido detallePedidoFullDTOToDetallePedido(DetallePedidoFullDTO detallePedidoFullDTO);

    DetallePedidoShortDTO detallePedidoToDetallePedidoShortDTO(DetallePedido detallePedido);

}
