package cl.GestionDrones.v1.Contratistas.mapper;

import cl.GestionDrones.v1.Contratistas.dto.CreateContratistaRequest;
import cl.GestionDrones.v1.Contratistas.dto.UpdateContratistaRequest;
import cl.GestionDrones.v1.Contratistas.model.Contratista;

public class ContratistaMapper {
    /**
     * Convierte CreateContratistaRequest a Contratista (para POST).
     * El ID se pasa como 0L (Long) ya que la base de datos lo autogenerará.
     */
    public static Contratista toModel(CreateContratistaRequest request) {
        return new Contratista(
                0, // CORREGIDO: Se usa 0L para especificar explícitamente el tipo long
                request.idEmpresaProveedora(), // Asegúrate de que el DTO devuelva long
                request.rut(),
                request.nombreEmpresa(),
                request.telefono(),
                request.contactoEmail(),
                request.estado()
        );
    }

    /**
     * Convierte UpdateContratistaRequest a Contratista (para PUT).
     * El ID se obtiene del path parameter de la URL.
     */
    // CORREGIDO: Cambiado de 'int id' a 'long id'
    public static Contratista toModel(long id, UpdateContratistaRequest request) {
        return new Contratista(
                id, // ID de tipo long proveniente del path parameter
                request.idEmpresaProveedora(), // Asegúrate de que el DTO devuelva long
                request.rut(),
                request.nombreEmpresa(),
                request.telefono(),
                request.contactoEmail(),
                request.estado()
        );
    }
}