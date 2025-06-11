package ElBuenSabor.ProyectoFinal.Mappers;

import ElBuenSabor.ProyectoFinal.DTO.SucursalCreateUpdateDTO;
import ElBuenSabor.ProyectoFinal.DTO.SucursalDTO;
import ElBuenSabor.ProyectoFinal.Entities.Categoria;
import ElBuenSabor.ProyectoFinal.Entities.Domicilio;
import ElBuenSabor.ProyectoFinal.Entities.Empresa;
import ElBuenSabor.ProyectoFinal.Entities.Sucursal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        DomicilioMapper.class,
        CategoriaMapper.class
})
public interface SucursalMapper {

    // Entidad a DTO completo (con categorías, domicilio)
    @Mapping(source = "baja", target = "estaDadoDeBaja")
    SucursalDTO toDTO(Sucursal sucursal);

    // DTO de creación/actualización a entidad
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedidos", ignore = true)
    @Mapping(target = "promociones", ignore = true)
    @Mapping(target = "empresa", source = "empresaId")
    @Mapping(target = "categorias", source = "categoriaIds")
    @Mapping(source = "estaDadoDeBaja", target = "baja")
    Sucursal toEntity(SucursalCreateUpdateDTO dto);

    // Métodos auxiliares
    default Empresa mapEmpresa(Long id) {
        if (id == null) return null;
        Empresa empresa = new Empresa();
        empresa.setId(id);
        return empresa;
    }

    default List<Categoria> mapCategorias(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream().map(id -> {
            Categoria c = new Categoria();
            c.setId(id);
            return c;
        }).toList();
    }
}