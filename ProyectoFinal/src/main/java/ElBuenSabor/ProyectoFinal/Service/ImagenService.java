package ElBuenSabor.ProyectoFinal.Service;


import ElBuenSabor.ProyectoFinal.Entities.Imagen;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImagenService {
    Imagen upload(MultipartFile file);
    void deleteById(Long id);
    List<Imagen> findAll();
    Imagen findById(Long id);
}
