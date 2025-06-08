package ElBuenSabor.ProyectoFinal.Mapper;

import ElBuenSabor.ProyectoFinal.DTO.ArticuloManufacturadoDetalleDTO;
import ElBuenSabor.ProyectoFinal.Entities.ArticuloManufacturadoDetalle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ArticuloManufacturadoDetalleMapper {

    ArticuloManufacturadoDetalleDTO articuloManufacturadoDetalleToArticuloManufacturadoDetalleDTO(ArticuloManufacturadoDetalle articuloManufacturadoDetalle);
    ArticuloManufacturadoDetalle articuloManufactudadoDetalleDtoToArticuloManufacturadoDetalle(ArticuloManufacturadoDetalleDTO articuloManufacturadoDetalleDTO);

}
