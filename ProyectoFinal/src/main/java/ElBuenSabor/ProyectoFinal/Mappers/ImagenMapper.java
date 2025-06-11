package ElBuenSabor.ProyectoFinal.Mappers;

import ElBuenSabor.ProyectoFinal.DTO.ImagenDTO;
import ElBuenSabor.ProyectoFinal.Entities.Imagen;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ImagenMapper {

    // Para mostrar en la respuesta
    @Mapping(source = "baja", target = "estaDadoDeBaja")
    ImagenDTO toDTO(Imagen imagen);

    // Para crear o actualizar desde un DTO
    @Mapping(target = "id", source = "id")
    @Mapping(target = "denominacion", source = "denominacion") // URL
    @Mapping(source = "estaDadoDeBaja", target = "baja")
    Imagen toEntity(ImagenDTO dto);

    // Método útil si solo recibís el ID de imagen (ej: ClienteRegistroDTO)
    default Imagen fromId(Long id) {
        if (id == null) return null;
        Imagen imagen = new Imagen();
        imagen.setId(id);
        return imagen;
    }
}