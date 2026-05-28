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
                
                // IF: Si la lista está vacía, podrías decidir enviar un 204 No Content
                if (contratistas.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                
                return ResponseEntity.ok(contratistas);
        }

        @PostMapping
        public ResponseEntity<Contratista> agregarContratista(@Valid @RequestBody CreateContratistaRequest request) {
                // IF: Validación manual del RUT en el controlador antes de procesar
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
        public ResponseEntity<Contratista> buscarContratista(@PathVariable int id) {
                Contratista contratista = contratistaService.getContratistaId(id);
                
                // IF: Si no se encuentra el contratista por ID
                if (contratista == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                
                return ResponseEntity.ok(contratista);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Contratista> actualizarContratista(@PathVariable int id,
                        @Valid @RequestBody UpdateContratistaRequest request) {
                
                // IF: Validación del RUT que viene en la petición de actualización
                if (request.rut() == null || request.rut().contains(" ")) {
                        throw new RutInvalidoException(request.rut(), "El RUT modificado no puede contener espacios en blanco.");
                }

                Contratista contratistaActualizado = contratistaService.updateContratista(ContratistaMapper.toModel(id, request));
                
                if (contratistaActualizado == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                
                return ResponseEntity.ok(contratistaActualizado);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminarContratista(@PathVariable int id) {
                // IF: Validación del ID antes de proceder a borrar
                if (id <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                contratistaService.deleteContratista(id);
                return ResponseEntity.noContent().build(); 
        }

        @GetMapping("/total")
        public ResponseEntity<Integer> totalContratistas() {
                int total = contratistaService.totalContratistasV2();
                
                // IF: Si el total es un número negativo por algún error de sincronización
                if (total < 0) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
                }
                
                return ResponseEntity.ok(total);
        }

        @GetMapping("/rut/{rut}")
        public ResponseEntity<List<Contratista>> selectPorRut(@PathVariable String rut) {
                // IF: Si el parámetro RUT enviado por URL está vacío o tiene un formato absurdo
                if (rut == null || rut.trim().length() < 8) {
                        // Aquí gatillamos tu excepción personalizada directamente desde el controlador
                        throw new RutInvalidoException(rut, "El formato de consulta de RUT en la URL es inválido.");
                }

                List<Contratista> contratistas = contratistaService.obtenerPorRut(rut);
                
                // IF: Si no hay contratistas asociados a ese RUT exacto
                if (contratistas.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(contratistas);
                }
                
                return ResponseEntity.ok(contratistas);
        }

        /**
         * NUEVO ENDPOINT: Obtiene contratistas filtrados por Empresa Proveedora.
         * Requerimiento crítico del negocio para la visibilidad de flotas de DroneChile SpA.
         */
        @GetMapping("/proveedora/{idEmpresaProveedora}")
        public ResponseEntity<List<Contratista>> listarPorEmpresaProveedora(@PathVariable int idEmpresaProveedora) {
                if (idEmpresaProveedora <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
                
                List<Contratista> contratistas = contratistaService.obtenerPorEmpresaProveedora(idEmpresaProveedora);
                return ResponseEntity.ok(contratistas);
        }
}