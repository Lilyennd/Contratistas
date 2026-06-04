package cl.GestionDrones.v1.Contratistas.mapper;

import cl.GestionDrones.v1.Contratistas.dto.CreateContratistaRequest;
import cl.GestionDrones.v1.Contratistas.dto.UpdateContratistaRequest;
import cl.GestionDrones.v1.Contratistas.model.Contratista;

public class ContratistaMapper {

    public static Contratista toModel(CreateContratistaRequest request) {
        Contratista contratista = new Contratista();
        
        contratista.setRut(request.rut());
        contratista.setnombreEmpresa(request.nombreEmpresa());
        contratista.setTelefono(request.telefono());
        contratista.setContactoEmail(request.contactoEmail());
        contratista.setEstado(request.estado());
        
        return contratista;
    }

    public static Contratista toModel(long id, UpdateContratistaRequest request) {
        Contratista contratista = new Contratista();
        
        contratista.setId(id); 
        contratista.setRut(request.rut());
        contratista.setnombreEmpresa(request.nombreEmpresa());
        contratista.setTelefono(request.telefono());
        contratista.setContactoEmail(request.contactoEmail());
        contratista.setEstado(request.estado());
        
        return contratista;
    }
}