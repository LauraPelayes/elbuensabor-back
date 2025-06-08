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
public class ArticuloInsumoFullDTO {

    private Long id;
    private String denominacion;
    private Double precioVenta;
    private Double precioCompra;
    private Double stockActual;
    private Double stockMaximo;
    private Boolean esParaElaborar;
    private UnidadMedida unidadMedida;
    private Imagen imagen;
    //DetallePedido??
    //Categoria??
    //Promocion??

//    private UnidadMedidaDTO unidadMedida; // For response
//    private Long categoriaId;
//    private CategoriaDTO categoria; // For response
//    private ImagenDTO imagen; // Could be just an ID for request, or a nested DTO
//    private Long imagenId; // For create/update if imagen is separate

}