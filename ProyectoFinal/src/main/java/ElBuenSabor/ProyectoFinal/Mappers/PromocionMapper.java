package ElBuenSabor.ProyectoFinal.Mappers;

import ElBuenSabor.ProyectoFinal.DTO.PromocionDTO;
import ElBuenSabor.ProyectoFinal.Entities.ArticuloManufacturado;
import ElBuenSabor.ProyectoFinal.Entities.Imagen;
import ElBuenSabor.ProyectoFinal.Entities.Promocion;
import ElBuenSabor.ProyectoFinal.Entities.Sucursal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = { ImagenMapper.class, ArticuloManufacturadoMapper.class })
public interface PromocionMapper {

    // Promocion -> PromocionDTO
    @Mapping(target = "imagenId", source = "imagen.id")
    @Mapping(target = "articuloManufacturadoIds", source = "articulosManufacturados", qualifiedByName = "toArticuloIds")
    @Mapping(target = "sucursalIds", source = "sucursales", qualifiedByName = "toSucursalIds")
    @Mapping(source = "baja", target = "estaDadoDeBaja")
    PromocionDTO toDTO(Promocion promocion);

    // PromocionDTO -> Promocion
    @Mapping(target = "imagen", source = "imagenId", qualifiedByName = "toImagen")
    @Mapping(target = "articulosManufacturados", source = "articuloManufacturadoIds", qualifiedByName = "toArticuloEntities")
    @Mapping(target = "sucursales", source = "sucursalIds", qualifiedByName = "toSucursalEntities")
    @Mapping(source = "estaDadoDeBaja", target = "baja")
    Promocion toEntity(PromocionDTO dto);

    // Métodos auxiliares
    @Named("toArticuloIds")
    default List<Long> toArticuloIds(List<ArticuloManufacturado> articulos) {
        if (articulos == null) return null;
        return articulos.stream().map(ArticuloManufacturado::getId).toList();
    }

    @Named("toSucursalIds")
    default Set<Long> toSucursalIds(Set<Sucursal> sucursales) {
        if (sucursales == null) return null;
        return sucursales.stream().map(Sucursal::getId).collect(Collectors.toSet());
    }

    @Named("toImagen")
    default Imagen toImagen(Long id) {
        if (id == null) return null;
        Imagen img = new Imagen();
        img.setId(id);
        return img;
    }

    @Named("toArticuloEntities")
    default List<ArticuloManufacturado> toArticuloEntities(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream().map(id -> {
            ArticuloManufacturado a = new ArticuloManufacturado();
            a.setId(id);
            return a;
        }).toList();
    }

    @Named("toSucursalEntities")
    default Set<Sucursal> toSucursalEntities(Set<Long> ids) {
        if (ids == null) return null;
        return ids.stream().map(id -> {
            Sucursal s = new Sucursal();
            s.setId(id);
            return s;
        }).collect(Collectors.toSet());
    }
}