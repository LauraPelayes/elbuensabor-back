package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.DTO.ImagenDTO;
import ElBuenSabor.ProyectoFinal.Entities.Imagen;
import ElBuenSabor.ProyectoFinal.Repositories.ImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImagenServiceImpl extends BaseServiceImpl<Imagen, Long> implements ImagenService {

    private final ImagenRepository imagenRepository;

    @Autowired
    public ImagenServiceImpl(ImagenRepository imagenRepository) {
        super(imagenRepository);
        this.imagenRepository = imagenRepository;
    }

    @Override
    @Transactional
    public Imagen createImagen(ImagenDTO dto) throws Exception {
        try {
            if (dto.getDenominacion() == null || dto.getDenominacion().trim().isEmpty()){
                throw new Exception("La denominación (URL) de la imagen no puede estar vacía.");
            }
            Imagen imagen = Imagen.builder().denominacion(dto.getDenominacion()).build();
            return imagenRepository.save(imagen);
        } catch (Exception e) {
            throw new Exception("Error al crear la imagen: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Imagen updateImagen(Long id, ImagenDTO dto) throws Exception {
        try {
            if (dto.getDenominacion() == null || dto.getDenominacion().trim().isEmpty()){
                throw new Exception("La denominación (URL) de la imagen no puede estar vacía.");
            }
            Imagen imagen = imagenRepository.findById(id)
                    .orElseThrow(() -> new Exception("Imagen no encontrada con ID: " + id));
            imagen.setDenominacion(dto.getDenominacion());
            return imagenRepository.save(imagen);
        } catch (Exception e) {
            throw new Exception("Error al actualizar la imagen: " + e.getMessage(), e);
        }
    }
}