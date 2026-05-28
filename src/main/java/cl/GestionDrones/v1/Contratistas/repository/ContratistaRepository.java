package cl.GestionDrones.v1.Contratistas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.GestionDrones.v1.Contratistas.model.Contratista;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
// CORREGIDO: Se cambió 'Integer' por 'Long' para que coincida con el ID de la entidad
public interface ContratistaRepository extends JpaRepository<Contratista, Long> {

    // CORREGIDA: Cambiada la tabla 'Contratistas' por 'empresas_contratistas' para evitar errores en la BD
    @Query(value = "SELECT * FROM empresas_contratistas WHERE rut = :rut", nativeQuery = true)
    List<Contratista> selectPorRut(@Param("rut") String rut);

    // Método alternativo usando las convenciones de Spring Data (opcional, pero recomendado)
    List<Contratista> findByRut(String rut);

    @Query(value = "SELECT * FROM empresas_contratistas WHERE id_empresa_proveedora = :idEmpresaProveedora", nativeQuery = true)
    List<Contratista> selectPorEmpresaProveedora(@Param("idEmpresaProveedora") long idEmpresaProveedora);

    default int totalContratistas() {
        return (int) this.count(); 
    }
}
