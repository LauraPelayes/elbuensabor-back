package ElBuenSabor.ProyectoFinal.Mappers;

import ElBuenSabor.ProyectoFinal.DTO.PedidoCreateDTO;
import ElBuenSabor.ProyectoFinal.DTO.PedidoResponseDTO;
import ElBuenSabor.ProyectoFinal.Entities.Cliente;
import ElBuenSabor.ProyectoFinal.Entities.Domicilio;
import ElBuenSabor.ProyectoFinal.Entities.Pedido;
import ElBuenSabor.ProyectoFinal.Entities.Sucursal;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        ClienteMapper.class,
        DomicilioMapper.class,
        FacturaMapper.class,
        SucursalMapper.class,
        DetallePedidoMapper.class
})
public interface PedidoMapper {

    // Entity → ResponseDTO
    @Mapping(source = "baja", target = "estaDadoDeBaja")
    PedidoResponseDTO toDTO(Pedido pedido);

    // CreateDTO → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true) // Se setea desde servicio
    @Mapping(target = "horaEstimadaFinalizacion", ignore = true)
    @Mapping(target = "fechaPedido", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "totalCosto", ignore = true)
    @Mapping(target = "factura", ignore = true)
    @Mapping(target = "cliente", source = "clienteId")
    @Mapping(target = "domicilioEntrega", source = "domicilioEntregaId")
    @Mapping(target = "sucursal", source = "sucursalId")
    @Mapping(source = "estaDadoDeBaja", target = "baja")
    Pedido toEntity(PedidoCreateDTO pedidoCreateDTO);

    // Métodos auxiliares para convertir IDs a entidades
    default Cliente mapCliente(Long id) {
        if (id == null) return null;
        Cliente c = new Cliente();
        c.setId(id);
        return c;
    }

    default Domicilio mapDomicilio(Long id) {
        if (id == null) return null;
        Domicilio d = new Domicilio();
        d.setId(id);
        return d;
    }

    default Sucursal mapSucursal(Long id) {
        if (id == null) return null;
        Sucursal s = new Sucursal();
        s.setId(id);
        return s;
    }
}