package ElBuenSabor.ProyectoFinal.Utils;

import ElBuenSabor.ProyectoFinal.Entities.*;
import ElBuenSabor.ProyectoFinal.Service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Component
public class DataLoader implements CommandLineRunner {

    @Value("${app.data.loader.enabled:false}")
    private boolean dataLoaderEnabled;

    private final PaisService paisService;
    private final ProvinciaService provinciaService;
    private final LocalidadService localidadService;
    private final CategoriaService categoriaService;
    private final UnidadMedidaService unidadMedidaService;
    private final ImagenService imagenService;
    private final UsuarioService usuarioService;
    private final ClienteService clienteService;
    private final ArticuloInsumoService articuloInsumoService;
    private final ArticuloManufacturadoService articuloManufacturadoService;
    private final ArticuloManufacturadoDetalleService articuloManufacturadoDetalleService;
    private final DomicilioService domicilioService;
    private final PromocionService promocionService;
    private final SucursalService sucursalService;

    public DataLoader(PaisService paisService,
                      ProvinciaService provinciaService,
                      LocalidadService localidadService,
                      CategoriaService categoriaService,
                      UnidadMedidaService unidadMedidaService,
                      ImagenService imagenService,
                      UsuarioService usuarioService,
                      ClienteService clienteService,
                      ArticuloInsumoService articuloInsumoService,
                      ArticuloManufacturadoService articuloManufacturadoService,
                      ArticuloManufacturadoDetalleService articuloManufacturadoDetalleService,
                      DomicilioService domicilioService,
                      PromocionService promocionService,
                      SucursalService sucursalService) {
        this.paisService = paisService;
        this.provinciaService = provinciaService;
        this.localidadService = localidadService;
        this.categoriaService = categoriaService;
        this.unidadMedidaService = unidadMedidaService;
        this.imagenService = imagenService;
        this.usuarioService = usuarioService;
        this.clienteService = clienteService;
        this.articuloInsumoService = articuloInsumoService;
        this.articuloManufacturadoService = articuloManufacturadoService;
        this.articuloManufacturadoDetalleService = articuloManufacturadoDetalleService;
        this.domicilioService = domicilioService;
        this.promocionService = promocionService;
        this.sucursalService = sucursalService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (!dataLoaderEnabled) {
            System.out.println("DataLoader está deshabilitado. Saltando la carga de datos.");
            return;
        }

        if (existenDatosIniciales()) {
            System.out.println("Los datos iniciales ya existen. Saltando la carga de datos.");
            return;
        }

        System.out.println("Cargando datos de ejemplo...");

        try {
            Pais pais = paisService.save(Pais.builder().nombre("Argentina").build());
            Provincia provincia = provinciaService.save(Provincia.builder().nombre("Mendoza").pais(pais).build());
            Localidad localidad = localidadService.save(Localidad.builder().nombre("Maipú").provincia(provincia).build());

            Imagen img1 = imagenService.save(Imagen.builder().denominacion("https://example.com/img1.jpg").build());
            Imagen img2 = imagenService.save(Imagen.builder().denominacion("https://example.com/img2.jpg").build());
            Imagen img3 = imagenService.save(Imagen.builder().denominacion("https://example.com/img3.jpg").build());
            Imagen imgPromo = imagenService.save(Imagen.builder().denominacion("https://example.com/promo.jpg").build());

            Categoria categoria = categoriaService.save(Categoria.builder().denominacion("Comida").build());
            UnidadMedida unidad = unidadMedidaService.save(UnidadMedida.builder().denominacion("unidad").build());

            ArticuloManufacturado a1 = ArticuloManufacturado.builder()
                    .denominacion("Hamburguesa Clásica")
                    .precioVenta(1500.0)
                    .descripcion("Hamburguesa con lechuga y tomate")
                    .tiempoEstimadoMinutos(20)
                    .preparacion("Cocinar y armar")
                    .categoria(categoria)
                    .unidadMedida(unidad)
                    .imagen(img1)
                    .build();

            ArticuloManufacturado a2 = ArticuloManufacturado.builder()
                    .denominacion("Pizza Margarita")
                    .precioVenta(2000.0)
                    .descripcion("Pizza con queso y albahaca")
                    .tiempoEstimadoMinutos(30)
                    .preparacion("Hornear")
                    .categoria(categoria)
                    .unidadMedida(unidad)
                    .imagen(img2)
                    .build();

            ArticuloManufacturado a3 = ArticuloManufacturado.builder()
                    .denominacion("Ensalada César")
                    .precioVenta(1200.0)
                    .descripcion("Ensalada con pollo y crutones")
                    .tiempoEstimadoMinutos(15)
                    .preparacion("Mezclar ingredientes")
                    .categoria(categoria)
                    .unidadMedida(unidad)
                    .imagen(img3)
                    .build();

            articuloManufacturadoService.save(a1);
            articuloManufacturadoService.save(a2);
            articuloManufacturadoService.save(a3);

            Promocion promo = Promocion.builder()
                    .denominacion("Happy Hour Promocional")
                    .fechaDesde(LocalDate.now())
                    .fechaHasta(LocalDate.now().plusWeeks(2))
                    .horaDesde(LocalTime.of(18, 0))
                    .horaHasta(LocalTime.of(21, 0))
                    .precioPromocional(1000.0)
                    .tipoPromocion(TipoPromocion.HAPPY_HOUR)
                    .articulosManufacturados(List.of(a1, a2))
                    .build();

            promocionService.save(promo);

            System.out.println("Datos cargados correctamente.");
        } catch (Exception e) {
            System.err.println("Error al cargar datos de ejemplo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean existenDatosIniciales() {
        try {
            return !articuloManufacturadoService.findAll().isEmpty();
        } catch (Exception e) {
            return true;
        }
    }
}
