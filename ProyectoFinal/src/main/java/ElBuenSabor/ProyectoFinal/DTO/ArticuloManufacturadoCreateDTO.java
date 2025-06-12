package ElBuenSabor.ProyectoFinal.DTO;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloManufacturadoCreateDTO {

    private Long id;
    private String denominacion;
    private Double precioVenta;
    private Long imagenId;
    private Long unidadMedidaId;
    private Long categoriaId;

    private String descripcion;
    private Integer tiempoEstimadoMinutos;
    private String preparacion;

    private List<ArticuloManufacturadoDetalleCreateDTO> detalles;
}
