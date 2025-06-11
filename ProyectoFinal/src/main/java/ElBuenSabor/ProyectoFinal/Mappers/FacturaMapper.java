package ElBuenSabor.ProyectoFinal.Mappers;

import ElBuenSabor.ProyectoFinal.DTO.FacturaDTO;
import ElBuenSabor.ProyectoFinal.Entities.Factura;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Builder;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface FacturaMapper {

    @Mapping(source = "pedido.id", target = "pedidoId")
    @Mapping(source = "baja", target = "estaDadoDeBaja")
    FacturaDTO toDTO(Factura factura);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "estaDadoDeBaja", target = "baja")
    @Mapping(target = "pedido", ignore = true) // lo setea el service con pedidoId
    @Mapping(target = "formaPago", ignore = true) // también se setea en el service
    Factura toEntity(FacturaDTO facturaDTO);
}