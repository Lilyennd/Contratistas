package cl.GestionDrones.v1.Contratistas.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import cl.GestionDrones.v1.Contratistas.dto.AeronaveResponse;
import cl.GestionDrones.v1.Contratistas.dto.ConsolidadoOperacionResponse;
import cl.GestionDrones.v1.Contratistas.dto.DetalleVueloEnriquecido;
import cl.GestionDrones.v1.Contratistas.dto.PilotoResponse;
import cl.GestionDrones.v1.Contratistas.dto.PlanesDeVuelosResponse;
import cl.GestionDrones.v1.Contratistas.exception.ResourceNotFoundException;
import cl.GestionDrones.v1.Contratistas.exception.RutInvalidoException;
import cl.GestionDrones.v1.Contratistas.model.Contratista;
import cl.GestionDrones.v1.Contratistas.repository.ContratistaRepository;

@Service
public class ContratistaService {
    
    @Autowired
    private ContratistaRepository contratistaRepository;

    // Instanciamos WebClient para realizar las llamadas HTTP externas
    private final WebClient webClient = WebClient.builder().build();

    // DEFINICIÓN DE PUERTOS (Cambia estos números por los puertos reales en los que corren tus microservicios)
    private final String URL_PLANES = "http://localhost:8083/api/planes-vuelo"; 
    private final String URL_PILOTOS = "http://localhost:8081/api/v1/pilotos";      
    private final String URL_AERONAVES = "http://localhost:8082/api/v1/aeronaves";

    public ConsolidadoOperacionResponse obtenerConsolidadoPorContratista(long idContratista) {
        
        Contratista contratista = getContratistaId(idContratista);

        
        List<PlanesDeVuelosResponse> todosLosPlanes = webClient.get()
                .uri(URL_PLANES)
                .retrieve()
                .bodyToFlux(PlanesDeVuelosResponse.class) 
                .collectList()
                .block();

        if (todosLosPlanes == null) {
            todosLosPlanes = Collections.emptyList();
        }


        List<DetalleVueloEnriquecido> vuelosEnriquecidos = todosLosPlanes.stream()
            .map(plan -> {
                
                // Limpieza del RUN para el servicio de pilotos
                String runNumerico = plan.runPiloto().replaceAll("[^0-9]", "");
                int runLimpio = runNumerico.isEmpty() ? 0 : Integer.parseInt(runNumerico);
                
                // Consultamos al microservicio de Pilotos por HTTP usando su endpoint de búsqueda por RUN
                List<PilotoResponse> listaPilotos = webClient.get()
                        .uri(URL_PILOTOS + "/buscarPorRun/" + runLimpio)
                        .retrieve()
                        .bodyToFlux(PilotoResponse.class)
                        .collectList()
                        .block();

                PilotoResponse pilotoDto = (listaPilotos != null && !listaPilotos.isEmpty()) ? listaPilotos.get(0) : null;

                List<AeronaveResponse> listaAeronaves = webClient.get()
                        .uri(URL_AERONAVES + "/obtenerPorPatente/" + plan.patenteDron())
                        .retrieve()
                        .bodyToFlux(AeronaveResponse.class)
                        .collectList()
                        .block();

                AeronaveResponse aeronaveDto = (listaAeronaves != null && !listaAeronaves.isEmpty()) ? listaAeronaves.get(0) : null;

                return new DetalleVueloEnriquecido(plan, pilotoDto, aeronaveDto);
            })
            .toList();

        return new ConsolidadoOperacionResponse(
            contratista.getId(),
            contratista.getRut(),
            contratista.getnombreEmpresa(), 
            contratista.getTelefono(),
            contratista.getContactoEmail(),
            contratista.getEstado(),
            vuelosEnriquecidos
        );
    }

    public List<Contratista> getContratistas() { 
        return contratistaRepository.findAll(); 
    }
    
    public Contratista saveContratista(Contratista contratista) { 
        return contratistaRepository.save(contratista); 
    }
    
    public Contratista getContratistaId(long id) {
        return contratistaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El contratista con ID " + id + " no está registrado en la DGAC."));
    }
    
    public Contratista updateContratista(Contratista contratista) {
        if (!contratistaRepository.existsById(contratista.getId())) {
            throw new ResourceNotFoundException("No se puede actualizar: El contratista con ID " + contratista.getId() + " no existe.");
        }
        return contratistaRepository.save(contratista);
    }
    
    public String deleteContratista(long id) {
        if (!contratistaRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: El contratista con ID " + id + " no existe.");
        }
        contratistaRepository.deleteById(id);
        return "Contratista eliminado";
    }
    
    public int totalContratistas() { 
        return (int) contratistaRepository.count(); 
    }
    
    public int totalContratistasV2() { 
        return contratistaRepository.totalContratistas(); 
    }
    
    public List<Contratista> obtenerPorRut(String rut) {
        List<Contratista> listaResultados = contratistaRepository.findByRut(rut);
        if (listaResultados.isEmpty()) { 
            throw new RutInvalidoException(rut); 
        }
        return listaResultados;
    }
    
    public Long obtenerIdPorNombre(String nombre) {
        Long id = contratistaRepository.findIdByNombre(nombre);
        if (id == null) { 
            throw new RuntimeException("Empresa contratista no encontrada con el nombre: " + nombre); 
        }
        return id;
    }
}
