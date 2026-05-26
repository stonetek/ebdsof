package stonetek.com.ebdsoft.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import stonetek.com.ebdsoft.model.Igreja;

public interface IgrejaRepository extends JpaRepository<Igreja, Long>{
    
    List<Igreja> findAllByOrderByNomeAsc();

    Optional<Igreja> findByNome(String nome);
}
