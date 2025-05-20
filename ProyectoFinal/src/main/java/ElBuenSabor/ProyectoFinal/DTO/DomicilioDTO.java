package ElBuenSabor.ProyectoFinal.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DomicilioDTO {
    private Long id;
    private String calle;
    private Integer numero;
    private Integer cp;
    private Long localidadId; // For request
    private LocalidadDTO localidad; // For response
    // If a Domicilio can be unlinked or linked to different clients/sucursales
    // you might need a clienteId or sucursalId here for request DTOs.
}