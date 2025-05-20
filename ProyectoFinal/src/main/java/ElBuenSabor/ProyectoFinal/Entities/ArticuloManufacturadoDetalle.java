package ElBuenSabor.ProyectoFinal.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "articulo_manufacturado_detalle")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ArticuloManufacturadoDetalle extends BaseEntity {

    private Double cantidad; // Cantidad de insumo para este producto

    @ManyToOne
    @JoinColumn(name = "articulo_insumo_id")
    private ArticuloInsumo articuloInsumo;

    // La relación @ManyToOne con ArticuloManufacturado se gestiona desde ArticuloManufacturado
    // con el @JoinColumn(name = "articulo_manufacturado_id")
}
