package cl.GestionDrones.v1.Contratistas.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import cl.GestionDrones.v1.Contratistas.model.Contratista;

@Repository
public interface ContratistaRepository extends JpaRepository<Contratista, Long> {

    
    @Query(value = "SELECT * FROM empresas_contratistas WHERE id = :id", nativeQuery = true)
    Optional<Contratista> selectPorId(@Param("id") Long id);

    
    @Query(value = "SELECT * FROM empresas_contratistas WHERE rut = :rut", nativeQuery = true)
    List<Contratista> selectPorRut(@Param("rut") String rut);

    default int totalContratistas() {
        return (int) this.count();
    }
}
