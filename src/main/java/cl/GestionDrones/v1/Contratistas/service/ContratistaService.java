package cl.GestionDrones.v1.Contratistas.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    // ✅ Inyectar los WebClients definidos en WebClientConfig
    private final WebClient aeronavesWebClient;
    private final WebClient planesDeVuelosWebClient;
    private final WebClient pilotosWebClient;

    @Autowired
    public ContratistaService(
            @Qualifier("aeronavesWebClient") WebClient aeronavesWebClient,
            @Qualifier("planesDeVuelosWebClient") WebClient planesDeVuelosWebClient,
            @Qualifier("pilotosWebClient") WebClient pilotosWebClient) {
        this.aeronavesWebClient = aeronavesWebClient;
        this.planesDeVuelosWebClient = planesDeVuelosWebClient;
        this.pilotosWebClient = pilotosWebClient;
    }

    public ConsolidadoOperacionResponse obtenerConsolidadoPorRut(String rut) {
    List<Contratista> contratistas = contratistaRepository.selectPorRut(rut);
    if (contratistas.isEmpty()) {
        throw new ResourceNotFoundException("Contratista no encontrado con RUT: " + rut);
    }
    Contratista contratista = contratistas.get(0);

        List<PlanesDeVuelosResponse> planesVuelo;
        try {
            // ✅ Usar el WebClient inyectado, solo la URI relativa
            planesVuelo = planesDeVuelosWebClient.get()
                    .uri("/contratista/" + contratista.getRut())
                    .retrieve()
                    .bodyToFlux(PlanesDeVuelosResponse.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            planesVuelo = Collections.emptyList();
            System.out.println("🔴 Error al conectar con Planes de Vuelo: " + e.getMessage());
        }

        List<DetalleVueloEnriquecido> vuelosEnriquecidos = Collections.emptyList();

        if (planesVuelo != null && !planesVuelo.isEmpty()) {
            vuelosEnriquecidos = planesVuelo.stream().map(plan -> {
                PilotoResponse piloto = null;
                AeronaveResponse aeronave = null;

                try {
                    // ✅ URI relativa al baseUrl del bean
                    piloto = pilotosWebClient.get()
                            .uri("/run/" + plan.runPiloto())
                            .retrieve()
                            .bodyToMono(PilotoResponse.class)
                            .block();
                } catch (Exception e) {
                    System.out.println("⚠️ No se pudo obtener piloto para RUN: " + plan.runPiloto());
                }

                try {
                    aeronave = aeronavesWebClient.get()
                            .uri("/patente/" + plan.patenteDron())
                            .retrieve()
                            .bodyToMono(AeronaveResponse.class)
                            .block();
                } catch (Exception e) {
                    System.out.println("⚠️ No se pudo obtener aeronave para patente: " + plan.patenteDron());
                }

                return new DetalleVueloEnriquecido(plan, piloto, aeronave);
            }).collect(Collectors.toList());
        }

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
    
    // Corregido para usar tu query nativa selectPorId
    public Contratista getContratistaId(long id) {
        return contratistaRepository.selectPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("El contratista con ID " + id + " no está registrado en la DGAC."));
    }
    
    public Contratista updateContratista(Contratista contratista) {
        if (!contratistaRepository.existsById(contratista.getId())) {
            throw new ResourceNotFoundException("No se puede actualizar: El contratista con ID " + contratista.getId() + " no existe.");
        }
        return contratistaRepository.save(contratista);
    }
    
    public void deleteContratista(long id) {
        if (!contratistaRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: El contratista con ID " + id + " no existe.");
        }
        contratistaRepository.deleteById(id);
    }
    
    // Usamos exclusivamente tu método nativo centralizado
    public int totalContratistas() { 
        return contratistaRepository.totalContratistas(); 
    }

    // Buscador por RUT con Query Nativa
    public List<Contratista> obtenerPorRut(String rut) {
        List<Contratista> listaResultados = contratistaRepository.selectPorRut(rut);
        if (listaResultados.isEmpty()) { 
            throw new RutInvalidoException(rut, "No se encontró ningún contratista asociado al RUT proporcionado."); 
        }
        return listaResultados;
    }
}
