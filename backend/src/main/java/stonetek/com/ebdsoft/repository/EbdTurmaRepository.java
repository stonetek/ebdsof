package stonetek.com.ebdsoft.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import stonetek.com.ebdsoft.model.EbdTurma;
import stonetek.com.ebdsoft.model.Ebd;
import stonetek.com.ebdsoft.model.Turma;
import java.util.Optional;


public interface EbdTurmaRepository extends JpaRepository<EbdTurma, Long> {

    @Query("SELECT et.turma.id FROM EbdTurma et WHERE et.ebd.id = :ebdId")
    List<Long> findTurmaIdsByEbdId(@Param("ebdId") Long ebdId);

    boolean existsByEbdAndTurma(Ebd ebd, Turma turma);

    boolean existsByTurmaAndEbdNot(Turma turma, Ebd ebd);

    List<EbdTurma> findByEbdId(Long ebdId);


    @Query("SELECT et FROM EbdTurma et WHERE et.turma.id = :turmaId")
    List<EbdTurma> findByTurmaId(@Param("turmaId") Long turmaId);

    @Query("SELECT et FROM EbdTurma et WHERE et.turma.id = :turmaId")
    Optional<EbdTurma> findOptionalByTurmaId(@Param("turmaId") Long turmaId);


    @Query("SELECT et.turma FROM EbdTurma et WHERE et.ebd.id = :ebdId AND :idade BETWEEN et.turma.idadeMinima AND et.turma.idadeMaxima")
    Optional<Turma> findTurmaByEbdIdAndIdade(@Param("ebdId") Long ebdId, @Param("idade") Integer idade);

    List<Turma> findByEbd(Ebd ebd);


    @Query("SELECT et.turma FROM EbdTurma et WHERE et.ebd IN :ebds")
    List<Turma> findTurmasByEbds(List<Ebd> ebds);




    List<EbdTurma> findByEbdIn(List<Long> ebdIds);


    List<EbdTurma> findAllByTurmaId(Long turmaId);


    List<Turma> findTurmasByEbdId(Long id);

}
