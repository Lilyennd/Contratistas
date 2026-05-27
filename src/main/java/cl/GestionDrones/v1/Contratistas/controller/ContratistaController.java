package cl.GestionDrones.v1.Contratistas.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.GestionDrones.v1.Contratistas.dto.CreateContratistaRequest;
import cl.GestionDrones.v1.Contratistas.dto.UpdateContratistaRequest;
import cl.GestionDrones.v1.Contratistas.mapper.ContratistaMapper;
import cl.GestionDrones.v1.Contratistas.model.Contratista;
import cl.GestionDrones.v1.Contratistas.service.ContratistaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/contratistas")
public class ContratistaController {

        private final ContratistaService contratistaService;

        // Inyección por constructor
        public ContratistaController(ContratistaService contratistaService) {
                this.contratistaService = contratistaService;
        }

        @GetMapping
        public ResponseEntity<List<Contratista>> listarContratistas() {
                List<Contratista> contratistas = contratistaService.getContratistas();
                return ResponseEntity.ok(contratistas);
        }

        @PostMapping
        public ResponseEntity<Contratista> agregarContratista(@Valid @RequestBody CreateContratistaRequest request) {
                // @Valid ejecuta las validaciones de Jakarta (formato RUT, Email, etc.)
                Contratista nuevoContratista = contratistaService.saveContratista(ContratistaMapper.toModel(request));
                return ResponseEntity.status(HttpStatus.CREATED).body(nuevoContratista);
        }

        @GetMapping("{id}")
        public ResponseEntity<Contratista> buscarContratista(@PathVariable int id) {
                // El Service se encarga de lanzar ResourceNotFoundException si no existe
                Contratista contratista = contratistaService.getContratistaId(id);
                return ResponseEntity.ok(contratista);
        }

        @PutMapping("{id}")
        public ResponseEntity<Contratista> actualizarContratista(@PathVariable int id,
                        @Valid @RequestBody UpdateContratistaRequest request) {
                
                Contratista contratistaActualizado = contratistaService.updateContratista(ContratistaMapper.toModel(id, request));
                return ResponseEntity.ok(contratistaActualizado);
        }

        @DeleteMapping("{id}")
        public ResponseEntity<Void> eliminarContratista(@PathVariable int id) {
                contratistaService.deleteContratista(id);
                return ResponseEntity.noContent().build(); // 204 No Content
        }

        @GetMapping("/total")
        public ResponseEntity<Integer> totalContratistas() {
                int total = contratistaService.totalContratistasV2();
                return ResponseEntity.ok(total);
        }

        @GetMapping("/rut/{rut}")
        public ResponseEntity<List<Contratista>> selectPorRut(@PathVariable String rut) {
                List<Contratista> contratistas = contratistaService.obtenerPorRut(rut);
                return ResponseEntity.ok(contratistas);
        }
}