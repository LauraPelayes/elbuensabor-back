package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.DTO.ImagenDTO;
import ElBuenSabor.ProyectoFinal.Entities.Imagen;

public interface ImagenService extends BaseService<Imagen, Long> {
    Imagen createImagen(ImagenDTO dto) throws Exception;
    Imagen updateImagen(Long id, ImagenDTO dto) throws Exception;
}