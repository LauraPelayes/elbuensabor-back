package com.elbuensabor.proyectofinal.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProvinciaCreateUpdateDTO {
    //Request DTO
    private String nombre;
    private Long paisId;
}