package ElBuenSabor.ProyectoFinal.DTO;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloInsumoDTO {

    private Long id;
    private String denominacion;
    private Double precioVenta;
    private Long imagenId;
    private Long unidadMedidaId;
    private Long categoriaId;

    private Double precioCompra;
    private Double stockActual;
    private Double stockMinimo;
    private Boolean esParaElaborar;

}
