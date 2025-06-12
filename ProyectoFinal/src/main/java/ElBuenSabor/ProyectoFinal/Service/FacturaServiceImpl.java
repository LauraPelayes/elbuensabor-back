package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.Entities.Factura;
import ElBuenSabor.ProyectoFinal.Exceptions.ResourceNotFoundException;
import ElBuenSabor.ProyectoFinal.Repositories.FacturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository facturaRepository;

    @Override
    public List<Factura> findAll() {
        return facturaRepository.findAll();
    }

    @Override
    public Factura findById(Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + id));
    }

    @Override
    public Factura save(Factura factura) {
        return facturaRepository.save(factura);
    }

    @Override
    public Factura update(Long id, Factura factura) {
        Factura actual = findById(id);

        actual.setFechaFacturacion(factura.getFechaFacturacion());
        actual.setMpPaymentId(factura.getMpPaymentId());
        actual.setMpMerchantOrderId(factura.getMpMerchantOrderId());
        actual.setMpPreferenceId(factura.getMpPreferenceId());
        actual.setMpPaymentType(factura.getMpPaymentType());
        actual.setFormaPago(factura.getFormaPago());
        actual.setTotalVenta(factura.getTotalVenta());

        return facturaRepository.save(actual);
    }

    @Override
    public void deleteById(Long id) {
        facturaRepository.deleteById(id);
    }
}
