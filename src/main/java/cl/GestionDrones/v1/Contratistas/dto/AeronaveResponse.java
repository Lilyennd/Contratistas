package cl.GestionDrones.v1.Contratistas.dto;

import java.time.LocalDate;

public record AeronaveResponse(
    Long id,
    Long idEmpresaProveedora,
    String patente,
    String numeroSerie,
    String marca,
    String modelo,
    String estado,
    LocalDate fechaVencimientoSeguro
) {}
