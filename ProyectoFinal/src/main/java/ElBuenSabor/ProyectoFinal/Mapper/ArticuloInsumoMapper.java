package ElBuenSabor.ProyectoFinal.Mapper;

import ElBuenSabor.ProyectoFinal.DTO.ArticuloInsumoFullDTO;
import ElBuenSabor.ProyectoFinal.DTO.ArticuloInsumoShortDTO;
import ElBuenSabor.ProyectoFinal.Entities.ArticuloInsumo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

public interface ArticuloInsumoMapper {

    ArticuloInsumoFullDTO articuloInsumoToArticuloInsumoFullDTO(ArticuloInsumo articuloInsumo);
    ArticuloInsumo articuloInsumoFullDtoToArticuloInsumo(ArticuloInsumo articuloInsumo);

    ArticuloInsumoShortDTO articuloInsumoToArticuloInsumoShortDTO(ArticuloInsumo articuloInsumo);

}
