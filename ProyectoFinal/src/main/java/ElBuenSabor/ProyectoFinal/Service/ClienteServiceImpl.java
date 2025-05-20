import ElBuenSabor.ProyectoFinal.Entities.Cliente;
import ElBuenSabor.ProyectoFinal.Entities.Localidad;
import ElBuenSabor.ProyectoFinal.Entities.Pais;
import ElBuenSabor.ProyectoFinal.Entities.Provincia;
import ElBuenSabor.ProyectoFinal.Repositories.*;
import ElBuenSabor.ProyectoFinal.Service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Necesario para encriptar contraseñas
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClienteServiceImpl extends BaseServiceImpl<Cliente, Long> implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final DomicilioRepository domicilioRepository;
    private final LocalidadRepository localidadRepository;
    private final ProvinciaRepository provinciaRepository;
    private final PaisRepository paisRepository;
    private final PasswordEncoder passwordEncoder; // Para encriptar y verificar contraseñas

    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository, DomicilioRepository domicilioRepository,
                              LocalidadRepository localidadRepository, ProvinciaRepository provinciaRepository,
                              PaisRepository paisRepository, PasswordEncoder passwordEncoder) {
        super(clienteRepository); // Llama al constructor de BaseServiceImpl
        this.clienteRepository = clienteRepository;
        this.domicilioRepository = domicilioRepository;
        this.localidadRepository = localidadRepository;
        this.provinciaRepository = provinciaRepository;
        this.paisRepository = paisRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Cliente registrarCliente(ClienteRegistroDTO registroDTO) throws Exception {
        // Criterios de aceptación para el registro de cliente:
        // 1. Verificar que el email no exista [cite: 19]
        if (clienteRepository.existsByEmail(registroDTO.getEmail())) {
            throw new Exception("El email ya está registrado.");
        }

        // 2. Verificar que las contraseñas coincidan [cite: 18]
        if (!registroDTO.getPassword().equals(registroDTO.getConfirmPassword())) {
            throw new Exception("Las contraseñas no coinciden.");
        }

        // 3. Validar la fuerza de la contraseña (mínimo 8 caracteres, mayúscula, minúscula, símbolo) [cite: 16]
        if (!isValidPassword(registroDTO.getPassword())) {
            throw new Exception("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un símbolo.");
        }

        // 4. Validar formato de email [cite: 17]
        if (!isValidEmail(registroDTO.getEmail())) {
            throw new Exception("Formato de email inválido.");
        }

        try {
            // Crear o encontrar Pais
            Pais pais = paisRepository.findByNombre(registroDTO.getNombrePais());
            if (pais == null) {
                pais = Pais.builder().nombre(registroDTO.getNombrePais()).build();
                pais = paisRepository.save(pais);
            }

            // Crear o encontrar Provincia
            Provincia provincia = provinciaRepository.findByNombre(registroDTO.getNombreProvincia());
            if (provincia == null) {
                provincia = Provincia.builder().nombre(registroDTO.getNombreProvincia()).pais(pais).build();
                provincia = provinciaRepository.save(provincia);
            }

            // Crear o encontrar Localidad
            Localidad localidad = localidadRepository.findByNombre(registroDTO.getNombreLocalidad());
            if (localidad == null) {
                localidad = Localidad.builder().nombre(registroDTO.getNombreLocalidad()).provincia(provincia).build();
                localidad = localidadRepository.save(localidad);
            }

            // Crear Domicilio
            Domicilio domicilio = Domicilio.builder()
                    .calle(registroDTO.getCalle())
                    .numero(registroDTO.getNumero())
                    .cp(registroDTO.getCp())
                    .localidad(localidad)
                    .build();
            domicilio = domicilioRepository.save(domicilio);

            // Crear Cliente [cite: 20]
            Cliente cliente = Cliente.builder()
                    .nombre(registroDTO.getNombre())
                    .apellido(registroDTO.getApellido())
                    .telefono(registroDTO.getTelefono())
                    .email(registroDTO.getEmail())
                    .password(passwordEncoder.encode(registroDTO.getPassword())) // Encriptar contraseña [cite: 20]
                    .estaDadoDeBaja(false) // Por defecto no dado de baja [cite: 21]
                    .build();

            cliente.getDomicilios().add(domicilio); // Asignar domicilio al cliente
            cliente.setAuth0Id(registroDTO.getEmail()); // Opcional, si usas Auth0
            cliente.setUsername(registroDTO.getEmail()); // Usar email como username para simplificar [cite: 250]

            return clienteRepository.save(cliente); // Guarda el cliente en la base de datos
        } catch (Exception e) {
            throw new Exception("Error al registrar el cliente: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente loginCliente(LoginDTO loginDTO) throws Exception {
        // 1. Verificar si el email existe
        Cliente cliente = clienteRepository.findByEmail(loginDTO.getEmail());
        if (cliente == null) {
            throw new Exception("Credenciales inválidas. Email no encontrado.");
        }

        // 2. Verificar si el cliente está dado de baja [cite: 33]
        if (cliente.isEstaDadoDeBaja()) {
            throw new Exception("El cliente está dado de baja y no puede acceder al sistema.");
        }

        // 3. Verificar la contraseña encriptada [cite: 30]
        if (!passwordEncoder.matches(loginDTO.getPassword(), cliente.getPassword())) {
            throw new Exception("Credenciales inválidas. Contraseña incorrecta.");
        }

        // Si todo es correcto, retorna el cliente (la sesión se manejará a nivel de seguridad/controlador)
        return cliente;
    }

    @Override
    @Transactional
    public Cliente actualizarCliente(Long id, ClienteActualizacionDTO actualizacionDTO) throws Exception {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (!clienteOptional.isPresent()) {
            throw new Exception("Cliente no encontrado con ID: " + id);
        }

        Cliente cliente = clienteOptional.get();

        // Actualizar datos personales [cite: 40]
        cliente.setNombre(actualizacionDTO.getNombre());
        cliente.setApellido(actualizacionDTO.getApellido());
        cliente.setTelefono(actualizacionDTO.getTelefono());

        // Actualizar domicilio si se proporciona (o crear si no existe) [cite: 41]
        if (actualizacionDTO.getCalle() != null && !actualizacionDTO.getCalle().isEmpty()) {
            // Asumimos que el cliente tiene al menos un domicilio. Si no, se crearía uno nuevo.
            // Para simplificar, actualizaremos el primer domicilio del cliente o crearemos uno si no tiene.
            Domicilio domicilio = cliente.getDomicilios().isEmpty() ? new Domicilio() : cliente.getDomicilios().get(0);

            // Crear o encontrar Pais
            Pais pais = paisRepository.findByNombre(actualizacionDTO.getNombrePais());
            if (pais == null) {
                pais = Pais.builder().nombre(actualizacionDTO.getNombrePais()).build();
                pais = paisRepository.save(pais);
            }

            // Crear o encontrar Provincia
            Provincia provincia = provinciaRepository.findByNombre(actualizacionDTO.getNombreProvincia());
            if (provincia == null) {
                provincia = Provincia.builder().nombre(actualizacionDTO.getNombreProvincia()).pais(pais).build();
                provincia = provinciaRepository.save(provincia);
            }

            // Crear o encontrar Localidad
            Localidad localidad = localidadRepository.findByNombre(actualizacionDTO.getNombreLocalidad());
            if (localidad == null) {
                localidad = Localidad.builder().nombre(actualizacionDTO.getNombreLocalidad()).provincia(provincia).build();
                localidad = localidadRepository.save(localidad);
            }

            domicilio.setCalle(actualizacionDTO.getCalle());
            domicilio.setNumero(actualizacionDTO.getNumero());
            domicilio.setCp(actualizacionDTO.getCp());
            domicilio.setLocalidad(localidad);

            // Si es un nuevo domicilio, añadirlo a la lista de domicilios del cliente
            if (!cliente.getDomicilios().contains(domicilio)) { // Verificar si ya está en la lista
                cliente.getDomicilios().add(domicilio);
            }
            domicilioRepository.save(domicilio);
        }


        // Actualizar contraseña si se proporcionan las nuevas contraseñas [cite: 42]
        if (actualizacionDTO.getNewPassword() != null && !actualizacionDTO.getNewPassword().isEmpty()) {
            // Verificar que la contraseña actual sea correcta (opcional pero muy recomendado por seguridad)
            if (!passwordEncoder.matches(actualizacionDTO.getCurrentPassword(), cliente.getPassword())) {
                throw new Exception("La contraseña actual ingresada es incorrecta.");
            }
            // Verificar que las nuevas contraseñas coincidan [cite: 43]
            if (!actualizacionDTO.getNewPassword().equals(actualizacionDTO.getConfirmNewPassword())) {
                throw new Exception("Las nuevas contraseñas no coinciden."); [cite: 44]
            }
            // Validar la fuerza de la nueva contraseña [cite: 42]
            if (!isValidPassword(actualizacionDTO.getNewPassword())) {
                throw new Exception("La nueva contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un símbolo.");
            }
            cliente.setPassword(passwordEncoder.encode(actualizacionDTO.getNewPassword())); // Encriptar nueva contraseña [cite: 45]
        }

        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public void darBajaCliente(Long id) throws Exception { // [cite: 84]
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (!clienteOptional.isPresent()) {
            throw new Exception("Cliente no encontrado con ID: " + id);
        }
        Cliente cliente = clienteOptional.get();
        cliente.setEstaDadoDeBaja(true); // Marca como dado de baja
        clienteRepository.save(cliente);
    }

    @Transactional
    public void darAltaCliente(Long id) throws Exception {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (!clienteOptional.isPresent()) {
            throw new Exception("Cliente no encontrado con ID: " + id);
        }
        Cliente cliente = clienteOptional.get();
        cliente.setEstaDadoDeBaja(false); // Marca como dado de alta
        clienteRepository.save(cliente);
    }

    // Métodos de validación privados
    private boolean isValidPassword(String password) {
        // La contraseña deberá tener un mínimo de 8 (ocho) caracteres,
        // y tendrá que tener por lo menos una letra mayúscula, una letra minúscula y un símbolo. [cite: 16]
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") && // Al menos una mayúscula
                password.matches(".*[a-z].*") && // Al menos una minúscula
                password.matches(".*[!@#<span class="math-inline">%^&\*\(\)\_\+\\\\\-\=\\\\\[\\\\\]\{\};'\:\\",\.<\>/?\]\.\*"\); // Al menos un símbolo
\}
    private boolean isValidEmail\(String</338\> email\) \{
// El sistema tendrá que verificar que la dirección de mail haya sido escrita de forma correcta\.</341\> \[cite\: 17\]
// Implementación de validación de formato de email \(Regex simple\)
        String emailRegex \= "^\[a\-zA\-Z0\-9\_\+&\*\-\]\+\(?\:\\\\\.\[a\-zA\-Z0\-9\_\+&\*\-\]\+\)\*@\(?\:\[a\-zA\-Z0\-9\-\]\+\\\\\.\)\+\[a\-zA\-Z\]\{2,7\}</span>";
        return email.matches(emailRegex);
    }
}