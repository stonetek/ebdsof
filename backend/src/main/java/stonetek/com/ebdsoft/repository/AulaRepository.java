package stonetek.com.ebdsoft.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stonetek.com.ebdsoft.model.Aula;
import stonetek.com.ebdsoft.model.EbdTurma;
import stonetek.com.ebdsoft.model.Turma;

public interface AulaRepository extends JpaRepository<Aula, Long> {
    List<Aula> findAllByOrderByLicaoAsc();

    @Query("SELECT a FROM Aula a WHERE FUNCTION('YEAR', a.dia) = :ano AND a.trimestre = :trimestre")
    List<Aula> findByTrimestreAndAno(@Param("trimestre") String trimestre, @Param("ano") Integer ano);

    List<Aula> findByTrimestre(String trimestre);

    @Query("SELECT a FROM Aula a JOIN a.aulaTurmas at WHERE at.turma IN :turmas AND a.dia = :data")
    List<Aula> findByTurmasEData(Set<Turma> turmas, LocalDate data);

    // Pesquisa por mês e ano, onde o ano é opcional
    @Query("SELECT a FROM Aula a JOIN a.aulaTurmas at WHERE at.turma IN :turmas AND EXTRACT(MONTH FROM a.dia) = :mes AND EXTRACT(YEAR FROM a.dia) = COALESCE(:ano, EXTRACT(YEAR FROM CURRENT_DATE))")
    List<Aula> findByTurmasEMesEAno(@Param("turmas") Set<Turma> turmas, @Param("mes") Integer mes, @Param("ano") Integer ano);

    // Pesquisa por trimestre e ano, onde o ano é opcional
    @Query("SELECT a FROM Aula a JOIN a.aulaTurmas at WHERE at.turma IN :turmas AND EXTRACT(QUARTER FROM a.dia) = :trimestre AND EXTRACT(YEAR FROM a.dia) = COALESCE(:ano, EXTRACT(YEAR FROM CURRENT_DATE))")
    List<Aula> findByTurmasETrimestreEAno(@Param("turmas") Set<Turma> turmas, @Param("trimestre") Integer trimestre, @Param("ano") Integer ano);


    @Query("SELECT a FROM Aula a " +
            "JOIN a.aulaTurmas at " +
            "JOIN at.turma t " +
            "JOIN t.ebdTurmas et " +
            "JOIN et.ebd e " +
            "JOIN e.igrejaEbds ie " +
            "JOIN ie.igreja i " +
            "WHERE i.id = :idIgreja")
    List<Aula> findAulasByIgrejaId(@Param("idIgreja") Long idIgreja);

}