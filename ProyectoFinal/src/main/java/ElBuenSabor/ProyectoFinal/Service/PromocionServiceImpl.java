package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.DTO.ArticuloManufacturadoDTO;
import ElBuenSabor.ProyectoFinal.DTO.ImagenDTO;
import ElBuenSabor.ProyectoFinal.DTO.PromocionDTO;
import ElBuenSabor.ProyectoFinal.DTO.SucursalDTO;
import ElBuenSabor.ProyectoFinal.Entities.*;
import ElBuenSabor.ProyectoFinal.Exceptions.ResourceNotFoundException;
import ElBuenSabor.ProyectoFinal.Repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromocionServiceImpl implements PromocionService {

    private final PromocionRepository promocionRepository;

    @Override
    public List<Promocion> findAll() {
        return promocionRepository.findAll();
    }

    @Override
    public Promocion findById(Long id) {
        return promocionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promoción no encontrada con ID: " + id));
    }

    @Override
    public Promocion save(Promocion promocion) {
        return promocionRepository.save(promocion);
    }

    @Override
    public Promocion update(Long id, Promocion promocion) {
        Promocion actual = findById(id);
        actual.setDenominacion(promocion.getDenominacion());//Duplicated code fragment (8 lines long)
        actual.setFechaDesde(promocion.getFechaDesde());
        actual.setFechaHasta(promocion.getFechaHasta());
        actual.setHoraDesde(promocion.getHoraDesde());
        actual.setHoraHasta(promocion.getHoraHasta());
        actual.setDescripcionDescuento(promocion.getDescripcionDescuento());
        actual.setPrecioPromocional(promocion.getPrecioPromocional());
        actual.setTipoPromocion(promocion.getTipoPromocion());
        actual.setImagen(promocion.getImagen());
        actual.setArticulosManufacturados(promocion.getArticulosManufacturados());
        actual.setSucursales(promocion.getSucursales());
        return promocionRepository.save(actual);
    }

    @Override
    public void deleteById(Long id) {
        promocionRepository.deleteById(id);
    }
}
