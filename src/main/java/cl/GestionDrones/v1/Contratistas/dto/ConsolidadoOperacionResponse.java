package cl.GestionDrones.v1.Contratistas.dto;

import java.util.List;

public record ConsolidadoOperacionResponse(
        long idContratista,
        String rutEmpresa,
        String nombreEmpresa,
        String estadoContratista,
        List<DetalleVueloEnriquecido> vuelos) 
{}
