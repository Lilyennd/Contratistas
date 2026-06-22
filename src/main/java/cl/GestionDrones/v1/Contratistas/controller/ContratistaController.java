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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Contratistas", description = "Operaciones relacionadas con las empresas contratistas de drones")
@RestController
@RequestMapping("/api/v1/contratistas")
public class ContratistaController {

    private final ContratistaService contratistaService;

    public ContratistaController(ContratistaService contratistaService) {
        this.contratistaService = contratistaService;
    }

    @Operation(summary = "Obtener todos los contratistas", description = "Retorna una lista completa de todas las empresas contratistas registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de contratistas obtenida con éxito", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Contratista.class))),
        @ApiResponse(responseCode = "204", description = "No hay contenido disponible (Lista vacía)", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Contratista>> listarContratistas() {
        List<Contratista> contratistas = contratistaService.getContratistas();
        if (contratistas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contratistas);
    }

    @Operation(summary = "Crear un nuevo contratista", description = "Registra un nuevo contratista en el sistema validando previamente su formato de RUT")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Estructura JSON de la nueva empresa contratista a registrar",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = CreateContratistaRequest.class),
            examples = @ExampleObject(
                name = "Ejemplo de Nuevo Contratista",
                value = "{\n  \"rut\": \"76123456-K\",\n  \"nombreEmpresa\": \"AeroServicios SkyDrone Ltda\",\n  \"telefono\": \"+56912345678\",\n  \"contactoEmail\": \"contacto@skydrone.cl\",\n  \"estado\": \"ACTIVO\"\n}"
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Contratista creado exitosamente", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Contratista.class))),
        @ApiResponse(responseCode = "400", description = "El RUT no cumple con los requisitos mínimos de validación", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Contratista> agregarContratista(
        @Valid @RequestBody CreateContratistaRequest request
    ) {
        if (request.rut() == null || request.rut().trim().length() < 8) {
            throw new RutInvalidoException(request.rut(), "El RUT no cumple con el largo mínimo permitido.");
        }

        Contratista nuevoContratista = contratistaService.saveContratista(ContratistaMapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoContratista);
    }

    @Operation(summary = "Buscar contratista por ID", description = "Busca y retorna los detalles de un contratista específico utilizando su ID único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contratista encontrado correctamente", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Contratista.class))),
        @ApiResponse(responseCode = "404", description = "Contratista no encontrado", content = @Content)
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<Contratista> buscarContratista(
        @Parameter(description = "ID del contratista a consultar", required = true, example = "1")
        @PathVariable long id
    ) {
        Contratista contratista = contratistaService.getContratistaId(id);
        return ResponseEntity.ok(contratista);
    }

    @Operation(summary = "Actualizar contratista", description = "Modifica los datos de una empresa contratista existente de acuerdo con su ID")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Estructura JSON con los nuevos campos del contratista",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UpdateContratistaRequest.class),
            examples = @ExampleObject(
                name = "Ejemplo de Actualización de Contratista",
                value = "{\n  \"rut\": \"76123456-K\",\n  \"nombreEmpresa\": \"AeroServicios SkyDrone Internacional SpA\",\n  \"telefono\": \"+56987654321\",\n  \"contactoEmail\": \"operaciones@skydrone.cl\",\n  \"estado\": \"ACTIVO\"\n}"
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contratista actualizado exitosamente", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Contratista.class))),
        @ApiResponse(responseCode = "400", description = "RUT modified inválido o payload incorrecto", content = @Content),
        @ApiResponse(responseCode = "404", description = "La empresa contratista no existe en el sistema", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Contratista> actualizarContratista(
        @Parameter(description = "ID del contratista que se desea actualizar", required = true, example = "1")
        @PathVariable long id,
        @Valid @RequestBody UpdateContratistaRequest request
    ) {
        
        if (request.rut() == null || request.rut().contains(" ")) {
            throw new RutInvalidoException(request.rut(), "El RUT modificado no puede contener espacios en blanco.");
        }
        
        Contratista contratistaActualizado = contratistaService.updateContratista(ContratistaMapper.toModel(id, request));
        return ResponseEntity.ok(contratistaActualizado);
    }

    @Operation(summary = "Eliminar un contratista", description = "Elimina de forma física una empresa contratista mediante su identificador numérico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "244", description = "Contratista eliminado correctamente, sin cuerpo de retorno"),
        @ApiResponse(responseCode = "400", description = "ID proporcionado es menor o igual a cero", content = @Content),
        @ApiResponse(responseCode = "444", description = "Contratista no encontrado para eliminar", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarContratista(
        @Parameter(description = "ID de la empresa contratista a eliminar", required = true, example = "1")
        @PathVariable long id
    ) {
        if (id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        contratistaService.deleteContratista(id);
        return ResponseEntity.noContent().build(); 
    }

    @Operation(summary = "Obtener total de contratistas", description = "Devuelve el conteo total de empresas contratistas registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Conteo obtenido con éxito", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class)))
    })
    @GetMapping("/total")
    public ResponseEntity<Integer> totalContratistas() {
        int total = contratistaService.totalContratistas();
        return ResponseEntity.ok(total);
    }

    @Operation(summary = "Buscar contratistas por RUT", description = "Busca y retorna las empresas que coinciden con el RUT provisto en la URL")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contratistas encontrados con éxito", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Contratista.class))),
        @ApiResponse(responseCode = "400", description = "Formato de consulta de RUT en la URL es inválido", content = @Content)
    })
    @GetMapping("/rut/{rut}")
    public ResponseEntity<List<Contratista>> selectPorRut(
        @Parameter(description = "RUT completo del contratista a consultar", required = true, example = "76123456-K")
        @PathVariable String rut
    ) {
        if (rut == null || rut.trim().length() < 8) {
            throw new RutInvalidoException(rut, "El formato de consulta de RUT en la URL es inválido.");
        }
        List<Contratista> contratistas = contratistaService.obtenerPorRut(rut);
        return ResponseEntity.ok(contratistas);
    }

    @Operation(summary = "Obtener consolidado de operaciones", description = "Devuelve un resumen consolidado de las operaciones de la empresa contratista mediante su RUT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resumen consolidado de operaciones recuperado con éxito", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConsolidadoOperacionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Formato de RUT provisto en la URL es inválido", content = @Content),
        @ApiResponse(responseCode = "404", description = "No se encontraron operaciones registradas para el RUT de contratista", content = @Content)
    })
    @GetMapping("/{rut}/consolidado")
    public ResponseEntity<ConsolidadoOperacionResponse> getConsolidadoOperacionResponse(
        @Parameter(description = "RUT del contratista para el consolidado", required = true, example = "76123456-K")
        @PathVariable String rut
    ) {
        if (rut == null || rut.trim().length() < 8) {
            throw new RutInvalidoException(rut, "El formato de RUT en la URL es inválido.");
        }
        ConsolidadoOperacionResponse respuesta = contratistaService.obtenerConsolidadoPorRut(rut);
        return ResponseEntity.ok(respuesta);
    }
}