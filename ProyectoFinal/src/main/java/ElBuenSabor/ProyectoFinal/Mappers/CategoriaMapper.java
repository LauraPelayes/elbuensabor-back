package ElBuenSabor.ProyectoFinal.Mappers;

import ElBuenSabor.ProyectoFinal.DTO.CategoriaDTO;
import ElBuenSabor.ProyectoFinal.DTO.CategoriaShortDTO;
import ElBuenSabor.ProyectoFinal.Entities.*;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    @Mapping(source = "categoriaPadre.id", target = "categoriaPadreId")
    @Mapping(source = "sucursales", target = "sucursalIds")
    @Mapping(source = "baja", target = "estaDadoDeBaja")
    CategoriaDTO toDTO(Categoria categoria);


    CategoriaShortDTO toShortDTO(Categoria categoria);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "estaDadoDeBaja", target = "baja")
    @Mapping(target = "articulos", ignore = true)
    @Mapping(target = "subCategorias", ignore = true)
    @Mapping(source = "categoriaPadreId", target = "categoriaPadre", qualifiedByName = "mapCategoriaPadre")
    @Mapping(source = "sucursalIds", target = "sucursales", qualifiedByName = "mapSucursales")
    Categoria toEntity(CategoriaDTO dto);

    default List<Long> mapSucursalesToIds(Set<Sucursal> sucursales) {
        if (sucursales == null) return null;
        return sucursales.stream().map(Sucursal::getId).collect(Collectors.toList());
    }

    @Named("mapCategoriaPadre")
    default Categoria mapCategoriaPadre(Long id) {
        if (id == null) return null;
        Categoria cat = new Categoria();
        cat.setId(id);
        return cat;
    }

    @Named("mapSucursales")
    default Set<Sucursal> mapSucursales(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream().map(id -> {
            Sucursal s = new Sucursal();
            s.setId(id);
            return s;
        }).collect(Collectors.toSet());
    }
}