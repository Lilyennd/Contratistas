package cl.GestionDrones.v1.Contratistas.dto;

public record DetalleVueloEnriquecido(
        PlanesDeVuelosResponse planVuelo,
        PilotoResponse piloto,
        AeronaveResponse aeronave)
{}
