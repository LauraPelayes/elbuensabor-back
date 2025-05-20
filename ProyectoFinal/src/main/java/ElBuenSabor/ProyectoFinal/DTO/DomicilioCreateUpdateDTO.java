package ElBuenSabor.ProyectoFinal.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DomicilioCreateUpdateDTO {
    //RequestDTO
    private String calle;
    private Integer numero;
    private Integer cp;
    private Long localidadId;
}