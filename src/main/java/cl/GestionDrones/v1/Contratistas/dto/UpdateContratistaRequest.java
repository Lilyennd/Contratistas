package cl.GestionDrones.v1.Contratistas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * DTO para actualizar un contratista existente (PUT).
 * No incluye ID del contratista porque se obtiene del path parameter en la URL del endpoint,
 * pero sí incluye el ID de la empresa proveedora que lo administra.
 */
public record UpdateContratistaRequest(
        @NotNull(message = "El ID de la empresa proveedora es obligatorio")
        long idEmpresaProveedora,

        @NotBlank(message = "El RUT de la empresa es obligatorio")
        @Pattern(
            regexp = "^[0-9]{1,2}\\.[0-9]{3}\\.[0-9]{3}-[0-9kK]{1}$", 
            message = "El formato del RUT debe ser válido (Ej: 76.123.456-K)"
        )
        String rut,

        @NotBlank(message = "El nombre de la empresa o contratista es obligatorio")
        String nombreEmpresa,

        @NotBlank(message = "El teléfono de contacto es obligatorio")
        String telefono,

        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "El formato del correo electrónico debe ser válido")
        String contactoEmail,

        @NotBlank(message = "El estado no puede ser vacío")
        String estado // Ejemplo: "ACTIVO", "INACTIVO"
) {}