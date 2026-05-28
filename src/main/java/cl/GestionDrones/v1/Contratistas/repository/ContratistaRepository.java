package cl.GestionDrones.v1.Contratistas.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.GestionDrones.v1.Contratistas.model.Contratista;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
@Repository

public interface ContratistaRepository extends JpaRepository<Contratista, Integer> {
    @Query(value = "SELECT * FROM Contratistas WHERE rut = :rut", nativeQuery = true)
    List<Contratista> selectPorRut(@Param("rut") String rut);

    default int totalContratistas() {
        return (int) this.count(); // ← "this" se refiere a la instancia del repository
    }
    

}
