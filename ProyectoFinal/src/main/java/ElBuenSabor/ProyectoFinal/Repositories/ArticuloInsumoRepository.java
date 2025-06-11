package ElBuenSabor.ProyectoFinal.Repositories;

import ElBuenSabor.ProyectoFinal.Entities.ArticuloInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloInsumoRepository extends JpaRepository<ArticuloInsumo, Long> {
    @Query("SELECT a FROM ArticuloInsumo a WHERE a.stockActual <= a.stockMinimo")
    List<ArticuloInsumo> findArticulosBajoStockMinimo();
}
