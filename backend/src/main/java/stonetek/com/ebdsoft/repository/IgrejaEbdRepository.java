package stonetek.com.ebdsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import stonetek.com.ebdsoft.model.Ebd;
import stonetek.com.ebdsoft.model.Igreja;
import stonetek.com.ebdsoft.model.IgrejaEbd;

import java.util.List;
import java.util.Optional;

public interface IgrejaEbdRepository extends JpaRepository<IgrejaEbd, Long>{

    Boolean existsByIgrejaAndEbd(Igreja igreja, Ebd ebd );

    List<IgrejaEbd> findByIgrejaId(Long igrejaId);

    Optional<IgrejaEbd> findByEbdId(Long id);

    @Query("SELECT ie.ebd FROM IgrejaEbd ie WHERE ie.igreja.id = :igrejaId")
    List<Ebd> findEbdsByIgrejaId(Long igrejaId);


}
