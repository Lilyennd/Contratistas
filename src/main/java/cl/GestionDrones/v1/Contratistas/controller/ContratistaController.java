package cl.GestionDrones.v1.Contratistas.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                if (request.rut() == null || request.rut().length() < 8) {
                        throw new RutInvalidoException(request.rut(), "El RUT no cumple con el largo mínimo.");
                }

                Contratista nuevoContratista = contratistaService.saveContratista(ContratistaMapper.toModel(request));
                
                if (nuevoContratista == null) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
                
                return ResponseEntity.status(HttpStatus.CREATED).body(nuevoContratista);
        }

        
        @GetMapping("/{id}")
        public ResponseEntity<Contratista> buscarContratista(@PathVariable long id) {
                Contratista contratista = contratistaService.getContratistaId(id);
                
                if (contratista == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                
                return ResponseEntity.ok(contratista);
        }

        
        @PutMapping("/{id}")
        public ResponseEntity<Contratista> actualizarContratista(@PathVariable long id,
                        @Valid @RequestBody UpdateContratistaRequest request) {
                
                if (request.rut() == null || request.rut().contains(" ")) {
                        throw new RutInvalidoException(request.rut(), "El RUT modificado no puede contener espacios en blanco.");
                }
                Contratista contratistaActualizado = contratistaService.updateContratista(ContratistaMapper.toModel(id, request));
                if (contratistaActualizado == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                
                return ResponseEntity.ok(contratistaActualizado);
        }

        // CORREGIDO: cambiado 'int id' por 'long id'
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
                int total = contratistaService.totalContratistasV2();
                
                if (total < 0) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
                }
                
                return ResponseEntity.ok(total);
        }

        @GetMapping("/rut/{rut}")
        public ResponseEntity<List<Contratista>> selectPorRut(@PathVariable String rut) {
                if (rut == null || rut.trim().length() < 8) {
                        throw new RutInvalidoException(rut, "El formato de consulta de RUT en la URL es inválido.");
                }

                List<Contratista> contratistas = contratistaService.obtenerPorRut(rut);
                
                if (contratistas.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(contratistas);
                }
                
                return ResponseEntity.ok(contratistas);
        }


        @GetMapping("/id")
    public ResponseEntity<Map<String, Long>> obtenerIdPorNombre(@RequestParam String nombre) {
        try {
            Long id = contratistaService.obtenerIdPorNombre(nombre);
            Map<String, Long> response = Collections.singletonMap("id", id);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); 
        }
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