package cl.GestionDrones.v1.Contratistas.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.GestionDrones.v1.Contratistas.model.Contratista;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository

public interface ContratistaRepository extends JpaRepository<Contratista, Long> {
@Autowired
    
    @Query(value = "SELECT * FROM empresas_contratistas WHERE rut = :rut", nativeQuery = true)
    List<Contratista> selectPorRut(@Param("rut") String rut);

    
    List<Contratista> findByRut(String rut);

   

    default int totalContratistas() {
        return (int) this.count(); 
    }

    //metodo id
    @Query("SELECT c.id FROM Contratista c WHERE c.nombreEmpresa = :nombre")
    Long findIdByNombre(@Param("nombre") String nombre);

}
