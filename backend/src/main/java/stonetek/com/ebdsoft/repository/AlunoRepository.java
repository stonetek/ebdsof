package stonetek.com.ebdsoft.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stonetek.com.ebdsoft.model.Aluno;
import stonetek.com.ebdsoft.model.Turma;
import stonetek.com.ebdsoft.util.Area;

public interface AlunoRepository extends JpaRepository<Aluno, Long>{

    List<Aluno> findAllByOrderByNomeAsc();

    @Query("SELECT a FROM Aluno a WHERE MONTH(a.aniversario) = MONTH(CURRENT_DATE)")
    List<Aluno> findAlunosAniversariantesDoMes();

    @Query("SELECT a FROM Aluno a WHERE " +
            "CASE " +
            "WHEN EXTRACT(MONTH FROM a.aniversario) IN (1, 2, 3) THEN 1 " +
            "WHEN EXTRACT(MONTH FROM a.aniversario) IN (4, 5, 6) THEN 2 " +
            "WHEN EXTRACT(MONTH FROM a.aniversario) IN (7, 8, 9) THEN 3 " +
            "WHEN EXTRACT(MONTH FROM a.aniversario) IN (10, 11, 12) THEN 4 " +
            "END = " +
            "CASE " +
            "WHEN EXTRACT(MONTH FROM CURRENT_DATE) IN (1, 2, 3) THEN 1 " +
            "WHEN EXTRACT(MONTH FROM CURRENT_DATE) IN (4, 5, 6) THEN 2 " +
            "WHEN EXTRACT(MONTH FROM CURRENT_DATE) IN (7, 8, 9) THEN 3 " +
            "WHEN EXTRACT(MONTH FROM CURRENT_DATE) IN (10, 11, 12) THEN 4 " +
            "END")
    List<Aluno> findAlunosAniversariantesDoTrimestre();


    @Query("SELECT a FROM Aluno a JOIN a.alunoTurmas at WHERE at.turma = :turma")
    List<Aluno> findByTurmasContains(@Param("turma") Turma turma);


    Optional<Aluno> findByUsuarioId(Long usuarioId);


    @Query("SELECT a FROM Aluno a " +
            "JOIN a.alunoTurmas at " +
            "JOIN at.turma t " +
            "LEFT JOIN t.ebdTurmas et " +
            "LEFT JOIN et.ebd e " +
            "LEFT JOIN e.igrejaEbds ie " +
            "WHERE ie.igreja.id = :idIgreja")
    List<Aluno> findAllByIgrejaId(Long idIgreja);


    @Query("SELECT DISTINCT a FROM Aluno a " +
            "JOIN a.alunoTurmas at JOIN at.turma t " +
            "JOIN t.ebdTurmas et JOIN et.ebd e JOIN e.igrejaEbds ie " +
            "WHERE ie.igreja.id = :igrejaId AND EXTRACT(MONTH FROM a.aniversario) = EXTRACT(MONTH FROM CURRENT_DATE)")
    List<Aluno> findAlunosAniversariantesDoMesByIgrejaId(@Param("igrejaId") Long igrejaId);

    @Query("SELECT DISTINCT a FROM Aluno a " +
            "JOIN a.alunoTurmas at JOIN at.turma t " +
            "JOIN t.ebdTurmas et JOIN et.ebd e JOIN e.igrejaEbds ie " +
            "WHERE ie.igreja.id = :igrejaId AND " +
            "CASE " +
            "WHEN EXTRACT(MONTH FROM a.aniversario) IN (1, 2, 3) THEN 1 " +
            "WHEN EXTRACT(MONTH FROM a.aniversario) IN (4, 5, 6) THEN 2 " +
            "WHEN EXTRACT(MONTH FROM a.aniversario) IN (7, 8, 9) THEN 3 " +
            "WHEN EXTRACT(MONTH FROM a.aniversario) IN (10, 11, 12) THEN 4 " +
            "END = " +
            "CASE " +
            "WHEN EXTRACT(MONTH FROM CURRENT_DATE) IN (1, 2, 3) THEN 1 " +
            "WHEN EXTRACT(MONTH FROM CURRENT_DATE) IN (4, 5, 6) THEN 2 " +
            "WHEN EXTRACT(MONTH FROM CURRENT_DATE) IN (7, 8, 9) THEN 3 " +
            "WHEN EXTRACT(MONTH FROM CURRENT_DATE) IN (10, 11, 12) THEN 4 " +
            "END")
    List<Aluno> findAlunosAniversariantesDoTrimestreByIgrejaId(@Param("igrejaId") Long igrejaId);





    @Query("SELECT a FROM Aluno a " +
            "JOIN a.alunoTurmas at " +
            "WHERE at.turma.id = :turmaId AND a.area = :area")
    List<Aluno> findByTurmaAndArea(@Param("turmaId") Long turmaId, @Param("area") Area area);

    @Query("SELECT DISTINCT a FROM Aluno a LEFT JOIN FETCH a.alunoTurmas at LEFT JOIN FETCH a.alunoAulas aa")
    List<Aluno> findAllWithTurmasAndAulas();

    @Query("SELECT a FROM Aluno a WHERE a.area = :area")
    List<Aluno> findByArea(@Param("area") Area area);
}
