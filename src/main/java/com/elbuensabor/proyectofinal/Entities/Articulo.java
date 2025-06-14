package com.elbuensabor.proyectofinal.Entities;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "articulo")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class Articulo extends BaseEntity {

    protected String denominacion;
    protected Double precioVenta;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "imagen_id", referencedColumnName = "id")
    protected Imagen imagen;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    protected Categoria categoria;

    @Column(columnDefinition = "boolean default false")
    protected boolean estaDadoDeBaja = false;
}
