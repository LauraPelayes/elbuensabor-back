package ElBuenSabor.ProyectoFinal.Service;

import ElBuenSabor.ProyectoFinal.DTO.ArticuloInsumoDTO;
import ElBuenSabor.ProyectoFinal.DTO.ArticuloManufacturadoDTO;
import ElBuenSabor.ProyectoFinal.DTO.ArticuloManufacturadoDetalleDTO;
import ElBuenSabor.ProyectoFinal.Entities.*;
import ElBuenSabor.ProyectoFinal.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ArticuloServiceImpl extends BaseServiceImpl<Articulo, Long> implements ArticuloService {

    private final ArticuloRepository articuloRepository;
    private final ArticuloInsumoRepository articuloInsumoRepository;
    private final ArticuloManufacturadoRepository articuloManufacturadoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UnidadMedidaRepository unidadMedidaRepository;
    private final ImagenRepository imagenRepository; // Para asociar/crear imágenes
    private final ArticuloManufacturadoDetalleRepository articuloManufacturadoDetalleRepository;


    @Autowired
    public ArticuloServiceImpl(ArticuloRepository articuloRepository,
                               ArticuloInsumoRepository articuloInsumoRepository,
                               ArticuloManufacturadoRepository articuloManufacturadoRepository,
                               CategoriaRepository categoriaRepository,
                               UnidadMedidaRepository unidadMedidaRepository,
                               ImagenRepository imagenRepository,
                               ArticuloManufacturadoDetalleRepository articuloManufacturadoDetalleRepository) {
        super(articuloRepository);
        this.articuloRepository = articuloRepository;
        this.articuloInsumoRepository = articuloInsumoRepository;
        this.articuloManufacturadoRepository = articuloManufacturadoRepository;
        this.categoriaRepository = categoriaRepository;
        this.unidadMedidaRepository = unidadMedidaRepository;
        this.imagenRepository = imagenRepository;
        this.articuloManufacturadoDetalleRepository = articuloManufacturadoDetalleRepository;
    }

    // --- Métodos para ArticuloInsumo ---
    @Override
    @Transactional
    public ArticuloInsumo createArticuloInsumo(ArticuloInsumoDTO dto) throws Exception {
        try {
            ArticuloInsumo insumo = new ArticuloInsumo();
            insumo.setDenominacion(dto.getDenominacion());
            insumo.setPrecioVenta(dto.getPrecioVenta());
            insumo.setPrecioCompra(dto.getPrecioCompra());
            insumo.setStockActual(dto.getStockActual());
            insumo.setStockMinimo(dto.getStockMinimo());
            insumo.setEsParaElaborar(dto.getEsParaElaborar());

            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new Exception("Categoría no encontrada con ID: " + dto.getCategoriaId()));
            insumo.setCategoria(categoria);

            UnidadMedida unidadMedida = unidadMedidaRepository.findById(dto.getUnidadMedidaId())
                    .orElseThrow(() -> new Exception("Unidad de Medida no encontrada con ID: " + dto.getUnidadMedidaId()));
            insumo.setUnidadMedida(unidadMedida);

            if (dto.getImagenId() != null) {
                Imagen imagen = imagenRepository.findById(dto.getImagenId())
                        .orElseThrow(() -> new Exception("Imagen no encontrada con ID: " + dto.getImagenId()));
                insumo.setImagen(imagen);
            } else if (dto.getImagen() != null && dto.getImagen().getDenominacion() != null && !dto.getImagen().getDenominacion().isEmpty()) {
                // Crear nueva imagen si se proporciona la denominación (URL)
                Imagen nuevaImagen = Imagen.builder().denominacion(dto.getImagen().getDenominacion()).build();
                insumo.setImagen(imagenRepository.save(nuevaImagen));
            }

            return articuloInsumoRepository.save(insumo);
        } catch (Exception e) {
            throw new Exception("Error al crear Artículo Insumo: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ArticuloInsumo updateArticuloInsumo(Long id, ArticuloInsumoDTO dto) throws Exception {
        try {
            ArticuloInsumo insumo = articuloInsumoRepository.findById(id)
                    .orElseThrow(() -> new Exception("Artículo Insumo no encontrado con ID: " + id));

            insumo.setDenominacion(dto.getDenominacion());
            insumo.setPrecioVenta(dto.getPrecioVenta());
            insumo.setPrecioCompra(dto.getPrecioCompra());
            insumo.setStockActual(dto.getStockActual());
            insumo.setStockMinimo(dto.getStockMinimo());
            insumo.setEsParaElaborar(dto.getEsParaElaborar());

            if (dto.getCategoriaId() != null) {
                Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                        .orElseThrow(() -> new Exception("Categoría no encontrada con ID: " + dto.getCategoriaId()));
                insumo.setCategoria(categoria);
            }

            if (dto.getUnidadMedidaId() != null) {
                UnidadMedida unidadMedida = unidadMedidaRepository.findById(dto.getUnidadMedidaId())
                        .orElseThrow(() -> new Exception("Unidad de Medida no encontrada con ID: " + dto.getUnidadMedidaId()));
                insumo.setUnidadMedida(unidadMedida);
            }

            if (dto.getImagenId() != null) {
                Imagen imagen = imagenRepository.findById(dto.getImagenId())
                        .orElseThrow(() -> new Exception("Imagen no encontrada con ID: " + dto.getImagenId()));
                insumo.setImagen(imagen);
            } else if (dto.getImagen() != null && dto.getImagen().getDenominacion() != null && !dto.getImagen().getDenominacion().isEmpty()) {
                Imagen imagenExistente = insumo.getImagen();
                if (imagenExistente != null) {
                    imagenExistente.setDenominacion(dto.getImagen().getDenominacion());
                    insumo.setImagen(imagenRepository.save(imagenExistente));
                } else {
                    Imagen nuevaImagen = Imagen.builder().denominacion(dto.getImagen().getDenominacion()).build();
                    insumo.setImagen(imagenRepository.save(nuevaImagen));
                }
            }


            return articuloInsumoRepository.save(insumo);
        } catch (Exception e) {
            throw new Exception("Error al actualizar Artículo Insumo: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticuloInsumo> findAllArticulosInsumo() throws Exception {
        try {
            return articuloInsumoRepository.findAll();
        } catch (Exception e) {
            throw new Exception("Error al obtener todos los Artículos Insumo: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticuloInsumo> findArticulosInsumoByStockActualLessThanEqual(Double stockMinimo) throws Exception {
        try {
            return articuloInsumoRepository.findByStockActualLessThanEqual(stockMinimo); //
        } catch (Exception e) {
            throw new Exception("Error al buscar artículos por stock mínimo: " + e.getMessage());
        }
    }

    // --- Métodos para ArticuloManufacturado ---
    @Override
    @Transactional
    public ArticuloManufacturado createArticuloManufacturado(ArticuloManufacturadoDTO dto) throws Exception {
        try {
            ArticuloManufacturado manufacturado = new ArticuloManufacturado();
            manufacturado.setDenominacion(dto.getDenominacion());
            manufacturado.setPrecioVenta(dto.getPrecioVenta());
            manufacturado.setDescripcion(dto.getDescripcion());
            manufacturado.setTiempoEstimadoMinutos(dto.getTiempoEstimadoMinutos());
            manufacturado.setPreparacion(dto.getPreparacion());

            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new Exception("Categoría no encontrada con ID: " + dto.getCategoriaId()));
            manufacturado.setCategoria(categoria);

            // Para ArticuloManufacturado, la unidad de medida suele ser "Unidad" o similar por defecto.
            // Si se envía explícitamente, se usa; si no, se podría buscar una por defecto o permitir nulo.
            if(dto.getUnidadMedidaId() != null) {
                UnidadMedida unidadMedida = unidadMedidaRepository.findById(dto.getUnidadMedidaId())
                        .orElseThrow(() -> new Exception("Unidad de Medida no encontrada con ID: " + dto.getUnidadMedidaId()));
                manufacturado.setUnidadMedida(unidadMedida);
            } else {
                // Opcional: buscar y asignar una unidad de medida por defecto como "Unidad"
                UnidadMedida unidadPorDefecto = unidadMedidaRepository.findByDenominacion("Unidad");
                if (unidadPorDefecto == null) {
                    unidadPorDefecto = unidadMedidaRepository.save(UnidadMedida.builder().denominacion("Unidad").build());
                }
                manufacturado.setUnidadMedida(unidadPorDefecto);
            }


            if (dto.getImagenId() != null) {
                Imagen imagen = imagenRepository.findById(dto.getImagenId())
                        .orElseThrow(() -> new Exception("Imagen no encontrada con ID: " + dto.getImagenId()));
                manufacturado.setImagen(imagen);
            } else if (dto.getImagen() != null && dto.getImagen().getDenominacion() != null && !dto.getImagen().getDenominacion().isEmpty()) {
                Imagen nuevaImagen = Imagen.builder().denominacion(dto.getImagen().getDenominacion()).build();
                manufacturado.setImagen(imagenRepository.save(nuevaImagen));
            }

            // Guardar el manufacturado antes de los detalles para tener su ID
            ArticuloManufacturado savedManufacturado = articuloManufacturadoRepository.save(manufacturado);


            if (dto.getDetalles() != null && !dto.getDetalles().isEmpty()) {
                Set<ArticuloManufacturadoDetalle> detallesSet = new HashSet<>();
                for (ArticuloManufacturadoDetalleDTO detalleDTO : dto.getDetalles()) {
                    ArticuloInsumo insumo = articuloInsumoRepository.findById(detalleDTO.getArticuloInsumoId())
                            .orElseThrow(() -> new Exception("Artículo Insumo para detalle no encontrado con ID: " + detalleDTO.getArticuloInsumoId()));
                    ArticuloManufacturadoDetalle detalle = ArticuloManufacturadoDetalle.builder()
                            .cantidad(detalleDTO.getCantidad())
                            .articuloInsumo(insumo)
                            // La relación con ArticuloManufacturado se establece al añadir al set de la entidad padre
                            // o se puede setear aquí si el JoinColumn está en ArticuloManufacturadoDetalle
                            .build();
                    // detalle.setArticuloManufacturado(savedManufacturado); // Si el JoinColumn está en Detalle
                    detallesSet.add(articuloManufacturadoDetalleRepository.save(detalle));
                }
                savedManufacturado.setDetalles(detallesSet);
            }

            return articuloManufacturadoRepository.save(savedManufacturado); // Guardar de nuevo con los detalles asociados

        } catch (Exception e) {
            throw new Exception("Error al crear Artículo Manufacturado: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ArticuloManufacturado updateArticuloManufacturado(Long id, ArticuloManufacturadoDTO dto) throws Exception {
        try {
            ArticuloManufacturado manufacturado = articuloManufacturadoRepository.findById(id)
                    .orElseThrow(() -> new Exception("Artículo Manufacturado no encontrado con ID: " + id));

            manufacturado.setDenominacion(dto.getDenominacion());
            manufacturado.setPrecioVenta(dto.getPrecioVenta());
            manufacturado.setDescripcion(dto.getDescripcion());
            manufacturado.setTiempoEstimadoMinutos(dto.getTiempoEstimadoMinutos());
            manufacturado.setPreparacion(dto.getPreparacion());

            if (dto.getCategoriaId() != null) {
                Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                        .orElseThrow(() -> new Exception("Categoría no encontrada con ID: " + dto.getCategoriaId()));
                manufacturado.setCategoria(categoria);
            }

            if(dto.getUnidadMedidaId() != null) {
                UnidadMedida unidadMedida = unidadMedidaRepository.findById(dto.getUnidadMedidaId())
                        .orElseThrow(() -> new Exception("Unidad de Medida no encontrada con ID: " + dto.getUnidadMedidaId()));
                manufacturado.setUnidadMedida(unidadMedida);
            }

            if (dto.getImagenId() != null) {
                Imagen imagen = imagenRepository.findById(dto.getImagenId())
                        .orElseThrow(() -> new Exception("Imagen no encontrada con ID: " + dto.getImagenId()));
                manufacturado.setImagen(imagen);
            } else if (dto.getImagen() != null && dto.getImagen().getDenominacion() != null && !dto.getImagen().getDenominacion().isEmpty()) {
                Imagen imagenExistente = manufacturado.getImagen();
                if (imagenExistente != null) {
                    imagenExistente.setDenominacion(dto.getImagen().getDenominacion());
                    manufacturado.setImagen(imagenRepository.save(imagenExistente));
                } else {
                    Imagen nuevaImagen = Imagen.builder().denominacion(dto.getImagen().getDenominacion()).build();
                    manufacturado.setImagen(imagenRepository.save(nuevaImagen));
                }
            }

            // Manejo de detalles: Borrar existentes y añadir nuevos, o una lógica más compleja de diff.
            // Opción simple: borrar los detalles antiguos y crear los nuevos.
            // manufacturado.getDetalles().clear(); // Esto podría necesitar `orphanRemoval=true` y un save previo
            // O borrar directamente desde el repositorio
            // Esto puede ser ineficiente. Una mejor aproximación es calcular la diferencia.
            // Por simplicidad, si se reenvían los detalles, los reemplazaremos.

            // Primero, eliminar los detalles que ya no están o que se van a actualizar.
            // Esto requiere cuidado para no violar restricciones si ArticuloManufacturadoDetalle no tiene mappedBy a ArticuloManufacturado
            // Si ArticuloManufacturado es el dueño de la relación con CascadeType.ALL y orphanRemoval=true,
            // limpiar el set y añadir los nuevos debería funcionar.

            // Guardamos el manufacturado para que se apliquen los orphanRemoval si es el caso
            // y para actualizar campos básicos.
            articuloManufacturadoRepository.save(manufacturado); // Salvar antes de manipular colecciones managed

            // Si la colección de detalles es gestionada con orphanRemoval=true en la entidad ArticuloManufacturado:
            if (dto.getDetalles() != null) {
                // Eliminar detalles que ya no están en el DTO (si es necesario, aquí se simplifica reemplazando)
                // manufacturado.getDetalles().forEach(detalle -> articuloManufacturadoDetalleRepository.delete(detalle)); // Cuidado con detached entities
                // manufacturado.getDetalles().clear(); // Si orphanRemoval está bien configurado

                // Para una actualización robusta de la colección de detalles:
                // 1. Eliminar detalles que ya no existen en el DTO
                // 2. Actualizar detalles existentes
                // 3. Añadir nuevos detalles
                // Por ahora, una implementación más simple: limpiar y añadir

                // Se requiere que la entidad ArticuloManufacturado tenga CascadeType.ALL y orphanRemoval=true para sus 'detalles'
                // y que ArticuloManufacturadoDetalleRepository.delete sea llamado explícitamente o se maneje la cascada
                // Limpiar los detalles existentes del manufacturado
                // Esta es una forma de hacerlo si la relación está bien configurada:
                if (manufacturado.getDetalles() != null) {
                    // Es más seguro iterar y eliminar para evitar problemas con la modificación de la colección mientras se itera
                    Set<ArticuloManufacturadoDetalle> detallesActuales = new HashSet<>(manufacturado.getDetalles());
                    for(ArticuloManufacturadoDetalle amd : detallesActuales) {
                        // amd.setArticuloManufacturado(null); // Desvincular si es necesario antes de borrar
                        articuloManufacturadoDetalleRepository.delete(amd);
                    }
                    manufacturado.getDetalles().clear();
                } else {
                    manufacturado.setDetalles(new HashSet<>());
                }


                Set<ArticuloManufacturadoDetalle> nuevosDetallesSet = new HashSet<>();
                for (ArticuloManufacturadoDetalleDTO detalleDTO : dto.getDetalles()) {
                    ArticuloInsumo insumo = articuloInsumoRepository.findById(detalleDTO.getArticuloInsumoId())
                            .orElseThrow(() -> new Exception("Artículo Insumo para detalle no encontrado con ID: " + detalleDTO.getArticuloInsumoId()));
                    ArticuloManufacturadoDetalle detalle = ArticuloManufacturadoDetalle.builder()
                            .cantidad(detalleDTO.getCantidad())
                            .articuloInsumo(insumo)
                            .build();
                    // detalle.setArticuloManufacturado(manufacturado); // Si el JoinColumn está en Detalle
                    nuevosDetallesSet.add(articuloManufacturadoDetalleRepository.save(detalle));
                }
                manufacturado.setDetalles(nuevosDetallesSet);
            }


            return articuloManufacturadoRepository.save(manufacturado);
        } catch (Exception e) {
            throw new Exception("Error al actualizar Artículo Manufacturado: " + e.getMessage() + (e.getCause() != null ? " Causa: " + e.getCause().getMessage() : ""));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticuloManufacturado> findAllArticulosManufacturados() throws Exception {
        try {
            return articuloManufacturadoRepository.findAll();
        } catch (Exception e) {
            throw new Exception("Error al obtener todos los Artículos Manufacturados: " + e.getMessage());
        }
    }

    // --- Métodos generales de búsqueda ---
    @Override
    @Transactional(readOnly = true)
    public List<Articulo> findByDenominacionContainingIgnoreCase(String denominacion) throws Exception {
        try {
            return articuloRepository.findByDenominacionContainingIgnoreCase(denominacion); //
        } catch (Exception e) {
            throw new Exception("Error al buscar artículos por denominación: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Articulo> findByCategoriaId(Long categoriaId) throws Exception {
        try {
            return articuloRepository.findByCategoriaId(categoriaId); //
        } catch (Exception e) {
            throw new Exception("Error al buscar artículos por ID de categoría: " + e.getMessage());
        }
    }
}