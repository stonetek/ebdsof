package stonetek.com.ebdsoft.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stonetek.com.ebdsoft.model.Ebd;
import stonetek.com.ebdsoft.model.IgrejaEbd;

public interface EbdRepository extends JpaRepository<Ebd, Long> {

    List<Ebd> findAllByOrderByNomeAsc();

    Optional<Ebd> findByNome(String nome);

    @Query("SELECT e FROM Ebd e JOIN IgrejaEbd ie ON e.id = ie.ebd.id WHERE ie.igreja.id = :igrejaId")
    Optional<Ebd> findByIgrejaId(@Param("igrejaId") Long igrejaId);

}
