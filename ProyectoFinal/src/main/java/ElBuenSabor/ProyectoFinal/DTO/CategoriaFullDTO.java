package ElBuenSabor.ProyectoFinal.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaFullDTO {
    private Long id;
    private String denominacion;
    private Long categoriaPadreId; // For request, nullable
    private CategoriaFullDTO categoriaPadre; // For response, nullable
    private Set<CategoriaFullDTO> subCategorias; // For response
    private List<Long> sucursalIds; // For linking to sucursales on create/update
}