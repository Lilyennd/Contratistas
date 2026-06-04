package cl.GestionDrones.v1.Contratistas.exception;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
        System.out.println("✅ GlobalExceptionHandler DE CONTRATISTAS SE HA REGISTRADO CORRECTAMENTE");
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        System.out.println("🔴 GlobalExceptionHandler [Contratistas] - Errores de validación detectados");

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Error de validación en los datos del contratista"
        );

        problem.setTitle("Validation Error - Contratista");
        problem.setProperty("timestamp", Instant.now());


        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Valor inválido"
                ));

        problem.setProperty("errors", errors);

        System.out.println("🔴 Errores de validación encontrados: " + errors);
        return problem;
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleJsonParseError(HttpMessageNotReadableException ex) {
        System.out.println("🟡 Error de parseo JSON en módulo Contratistas");
        System.out.println("🟡 Mensaje: " + ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Error al procesar el JSON del contratista enviado"
        );

        problem.setTitle("JSON Parse Error - Contratista");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("detalle", ex.getMostSpecificCause().getMessage());
        return problem;
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {
        System.out.println("🟡 Contratista o recurso no encontrado");

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, 
                ex.getMessage()
        );

        problem.setTitle("Contratista Not Found");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {
        System.out.println("🔴 EXCEPCIÓN CAPTURADA EN CONTRATISTAS: " + ex.getClass().getName());
        System.out.println("🔴 Mensaje: " + ex.getMessage());
        ex.printStackTrace();

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno en el módulo de gestión de contratistas"
        );

        problem.setTitle("Internal Server Error - Contratistas");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("detalle", ex.getMessage());
        problem.setProperty("tipoExcepcion", ex.getClass().getSimpleName());
        return problem;
    }

    @ExceptionHandler(RutInvalidoException.class)
    public ProblemDetail handleRutInvalido(RutInvalidoException ex) {
        System.out.println("🟡 GlobalExceptionHandler [Contratistas] - Problema detectado con el RUT: " + ex.getRut());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );

        problem.setTitle("RUT Contratista Error");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("rutIngresado", ex.getRut()); // Muestra el RUT afectado en el JSON de respuesta
        
        return problem;
    }
}
