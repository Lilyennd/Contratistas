package cl.GestionDrones.v1.Contratistas.mapper;

import cl.GestionDrones.v1.Contratistas.dto.CreateContratistaRequest;
import cl.GestionDrones.v1.Contratistas.dto.UpdateContratistaRequest;
import cl.GestionDrones.v1.Contratistas.model.Contratista;

public class ContratistaMapper {
    /**
     * Convierte CreateContratistaRequest a Contratista (para POST).
     * El ID se pasa como 0 temporalmente ya que la base de datos lo autogenerará.
     */
    public static Contratista toModel(CreateContratistaRequest request) {
        return new Contratista(
                0, 
                request.rut(),
                request.razonSocial(),
                request.telefono(),
                request.contactoEmail(),
                request.estado()
        );
    }

    /**
     * Convierte UpdateContratistaRequest a Contratista (para PUT).
     * El ID se obtiene del path parameter de la URL.
     */
    public static Contratista toModel(int id, UpdateContratistaRequest request) {
        return new Contratista(
                id, // ID proveniente del path parameter
                request.rut(),
                request.razonSocial(),
                request.telefono(),
                request.contactoEmail(),
                request.estado()
        );
    }
}
