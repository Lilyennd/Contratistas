package cl.GestionDrones.v1.Contratistas.dto;

import java.util.List;

public record ConsolidadoOperacionResponse(
        long id,
        String rut,
        String nombreEmpresa,
        String telefono,
        String contactoEmail,
        String estado,
        List<DetalleVueloEnriquecido> vuelos) 
{}
