package ElBuenSabor.ProyectoFinal.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ArticuloManufacturadoShortDTO {

    //ArticuloManufacturado corto para por ej: Mostrar articulos de una categoria o en un layer de menu
    private Long id;
    private ImagenDTO imagen;
    private String denominacion;
    private Double precioVenta;
    private String descripcion;

}
