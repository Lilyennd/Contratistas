package cl.GestionDrones.v1.Contratistas.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.GestionDrones.v1.Contratistas.dto.ConsolidadoOperacionResponse;
import cl.GestionDrones.v1.Contratistas.dto.CreateContratistaRequest;
import cl.GestionDrones.v1.Contratistas.dto.UpdateContratistaRequest;
import cl.GestionDrones.v1.Contratistas.mapper.ContratistaMapper;
import cl.GestionDrones.v1.Contratistas.model.Contratista;
import cl.GestionDrones.v1.Contratistas.service.ContratistaService;
import cl.GestionDrones.v1.Contratistas.exception.RutInvalidoException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/contratistas")
public class ContratistaController {

    private final ContratistaService contratistaService;

    public ContratistaController(ContratistaService contratistaService) {
        this.contratistaService = contratistaService;
    }

    
    @GetMapping
    public ResponseEntity<List<Contratista>> listarContratistas() {
        List<Contratista> contratistas = contratistaService.getContratistas();
        if (contratistas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contratistas);
    }

    @PostMapping
    public ResponseEntity<Contratista> agregarContratista(@Valid @RequestBody CreateContratistaRequest request) {
        if (request.rut() == null || request.rut().trim().length() < 8) {
            throw new RutInvalidoException(request.rut(), "El RUT no cumple con el largo mínimo permitido.");
        }

        Contratista nuevoContratista = contratistaService.saveContratista(ContratistaMapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoContratista);
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<Contratista> buscarContratista(@PathVariable long id) {
        // Tu servicio lanza ResourceNotFoundException automáticamente si no existe
        Contratista contratista = contratistaService.getContratistaId(id);
        return ResponseEntity.ok(contratista);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Contratista> actualizarContratista(@PathVariable long id,
            @Valid @RequestBody UpdateContratistaRequest request) {
        
        if (request.rut() == null || request.rut().contains(" ")) {
            throw new RutInvalidoException(request.rut(), "El RUT modificado no puede contener espacios en blanco.");
        }
        
        Contratista contratistaActualizado = contratistaService.updateContratista(ContratistaMapper.toModel(id, request));
        return ResponseEntity.ok(contratistaActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarContratista(@PathVariable long id) {
        if (id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        contratistaService.deleteContratista(id);
        return ResponseEntity.noContent().build(); 
    }

    @GetMapping("/total")
    public ResponseEntity<Integer> totalContratistas() {
        int total = contratistaService.totalContratistas();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<List<Contratista>> selectPorRut(@PathVariable String rut) {
        if (rut == null || rut.trim().length() < 8) {
            throw new RutInvalidoException(rut, "El formato de consulta de RUT en la URL es inválido.");
        }
        List<Contratista> contratistas = contratistaService.obtenerPorRut(rut);
        return ResponseEntity.ok(contratistas);
    }

    @GetMapping("/{rut}/consolidado")
    public ResponseEntity<ConsolidadoOperacionResponse> getConsolidadoOperacionResponse(@PathVariable String rut) {
        if (rut == null || rut.trim().length() < 8) {
            throw new RutInvalidoException(rut, "El formato de RUT en la URL es inválido.");
        }
        ConsolidadoOperacionResponse respuesta = contratistaService.obtenerConsolidadoPorRut(rut);
        return ResponseEntity.ok(respuesta);
    }
}