package cl.GestionDrones.v1.Contratistas.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.GestionDrones.v1.Contratistas.model.Contratista;
import cl.GestionDrones.v1.Contratistas.repository.ContratistaRepository;
import cl.GestionDrones.v1.Contratistas.exception.ResourceNotFoundException;
import cl.GestionDrones.v1.Contratistas.exception.RutInvalidoException;

@Service
public class ContratistaService {
    @Autowired
    private ContratistaRepository contratistaRepository;

    public List<Contratista> getContratistas() {
        return contratistaRepository.findAll();
    }

    public Contratista saveContratista(Contratista contratista) {
        return contratistaRepository.save(contratista);
    }

    public Contratista getContratistaId(int id) {
        return contratistaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El contratista con ID " + id + " no está registrado en la DGAC."));
    }

    public Contratista updateContratista(Contratista contratista) {
        // Primero verificamos si existe para asegurar que es un PUT válido
        if (!contratistaRepository.existsById(contratista.getId())) {
            throw new ResourceNotFoundException("No se puede actualizar: El contratista con ID " + contratista.getId() + " no existe.");
        }
        return contratistaRepository.save(contratista);
    }

    public String deleteContratista(int id) {
        if (!contratistaRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: El contratista con ID " + id + " no existe.");
        }
        contratistaRepository.deleteById(id);
        return "Contratista eliminado";
    }

    // LA ACCIÓN LA HACE EL SERVICE
    public int totalContratistas() {
        return (int) contratistaRepository.count();
    }

    // LA ACCIÓN LA HACE EL REPOSITORIO
    public int totalContratistasV2() {
        return contratistaRepository.totalContratistas();
    }

    // Métodos de búsqueda personalizados para la DGAC
    public List<Contratista> obtenerPorRut(String rut) {
        // 1. Buscas en la base de datos (ejemplo conceptual)
        // List<Contratista> listaResultados = contratistaRepository.findByRut(rut);
        List<Contratista> listaResultados = java.util.Collections.emptyList(); // Simulación vacía

        // 2. Si no hay registros con ese RUT, lanzas la nueva excepción independiente
        if (listaResultados.isEmpty()) {
            throw new RutInvalidoException(rut);
        }

        return listaResultados;
    }

    public List<Contratista> obtenerPorEmpresaProveedora(int idEmpresaProveedora) {
        List<Contratista> contratistas = contratistaRepository.selectPorEmpresaProveedora(idEmpresaProveedora);
        
        if (contratistas.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron contratistas asociados a la empresa proveedora con ID " + idEmpresaProveedora);
        }
        
        return contratistas;
    }
}
