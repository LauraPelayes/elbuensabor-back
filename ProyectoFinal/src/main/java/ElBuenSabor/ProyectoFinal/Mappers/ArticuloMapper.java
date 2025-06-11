//package ElBuenSabor.ProyectoFinal.Mappers;
//
//import ElBuenSabor.ProyectoFinal.DTO.ArticuloInsumoDTO;
//import ElBuenSabor.ProyectoFinal.DTO.ArticuloManufacturadoDTO;
//import ElBuenSabor.ProyectoFinal.Entities.ArticuloInsumo;
//import ElBuenSabor.ProyectoFinal.Entities.ArticuloManufacturado;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//@Mapper(componentModel = "spring")
//public interface ArticuloMapper {
//
//    // --- Métodos para ArticuloInsumo ---
//    @Mapping(source = "categoria.id", target = "categoriaId")
//    @Mapping(source = "unidadMedida.id", target = "unidadMedidaId")
//    @Mapping(source = "imagen.id", target = "imagenId")
//    ArticuloInsumoDTO toInsumoDTO(ArticuloInsumo insumo);
//
//    @Mapping(source = "categoria.id", target = "categoriaId")
//    @Mapping(source = "imagen.id", target = "imagenId")
//    @Mapping(target = "unidadMedidaId", ignore = true)
//    @Mapping(target = "unidadMedida", ignore = true)
//    ArticuloManufacturadoDTO toManufacturadoDTO(ArticuloManufacturado manufacturado);
//
//
//
//    @Mapping(target = "id", ignore = true)
//    @Mapping(source = "estaDadoDeBaja", target = "baja")
//    @Mapping(target = "categoria", ignore = true)
//    @Mapping(target = "imagen", ignore = true)
//    @Mapping(target = "unidadMedida", ignore = true)
//    ArticuloInsumo toInsumoEntity(ArticuloInsumoDTO dto);
//
//
//    @Mapping(target = "id", ignore = true)
//    @Mapping(source = "estaDadoDeBaja", target = "baja")
//    @Mapping(target = "categoria", ignore = true)
//    @Mapping(target = "imagen", ignore = true)
//    @Mapping(target = "detalles", ignore = true)
//    ArticuloManufacturado toManufacturadoEntity(ArticuloManufacturadoDTO dto);
//
//}