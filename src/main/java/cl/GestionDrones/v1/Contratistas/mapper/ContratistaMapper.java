package cl.GestionDrones.v1.Contratistas.mapper;

import cl.GestionDrones.v1.Contratistas.dto.CreateContratistaRequest;
import cl.GestionDrones.v1.Contratistas.dto.UpdateContratistaRequest;
import cl.GestionDrones.v1.Contratistas.model.Contratista;

public class ContratistaMapper {

    public static Contratista toModel(CreateContratistaRequest request) {
        return new Contratista(
                0, 
                request.idEmpresaProveedora(), 
                request.rut(),
                request.nombreEmpresa(),
                request.telefono(),
                request.contactoEmail(),
                request.estado()
        );
    }


    public static Contratista toModel(long id, UpdateContratistaRequest request) {
        return new Contratista(
                id, 
                request.idEmpresaProveedora(), 
                request.rut(),
                request.nombreEmpresa(),
                request.telefono(),
                request.contactoEmail(),
                request.estado()
        );
    }
}