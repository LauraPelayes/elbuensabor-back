package ElBuenSabor.ProyectoFinal.DTO;

import ElBuenSabor.ProyectoFinal.Entities.Imagen;
import ElBuenSabor.ProyectoFinal.Entities.UnidadMedida;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ArticuloInsumoShortDTO {

    //Articulo corto para por ej: Menu desplegable de insumos.
    private Long id;
    private String denominacion;
    private Double stockActual;
    private UnidadMedida unidadMedida;
    private Imagen imagen;

}
