package stonetek.com.ebdsoft.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import stonetek.com.ebdsoft.model.Aluno;
import stonetek.com.ebdsoft.model.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long>{

    List<Professor> findAllByOrderByNomeAsc();

    Optional<Professor> findByUsuarioId(Long usuarioId);

    @Query("SELECT p FROM Professor p WHERE MONTH(p.aniversario) = MONTH(CURRENT_DATE)")
    List<Professor> findProfessoresAniversariantesDoMes();


    @Query("SELECT p FROM Professor p WHERE " +
            "CASE " +
            "WHEN EXTRACT(MONTH FROM p.aniversario) IN (1, 2, 3) THEN 1 " +
            "WHEN EXTRACT(MONTH FROM p.aniversario) IN (4, 5, 6) THEN 2 " +
            "WHEN EXTRACT(MONTH FROM p.aniversario) IN (7, 8, 9) THEN 3 " +
            "WHEN EXTRACT(MONTH FROM p.aniversario) IN (10, 11, 12) THEN 4 " +
            "END = " +
            "CASE " +
            "WHEN EXTRACT(MONTH FROM CURRENT_DATE) IN (1, 2, 3) THEN 1 " +
            "WHEN EXTRACT(MONTH FROM CURRENT_DATE) IN (4, 5, 6) THEN 2 " +
            "WHEN EXTRACT(MONTH FROM CURRENT_DATE) IN (7, 8, 9) THEN 3 " +
            "WHEN EXTRACT(MONTH FROM CURRENT_DATE) IN (10, 11, 12) THEN 4 " +
            "END")
    List<Professor> findProfessoresAniversariantesDoTrimestre();


    @Query("SELECT a FROM Professor a " +
            "JOIN a.professorTurmas at " +
            "JOIN at.turma t " +
            "LEFT JOIN t.ebdTurmas et " +
            "LEFT JOIN et.ebd e " +
            "LEFT JOIN e.igrejaEbds ie " +
            "WHERE ie.igreja.id = :idIgreja")
    List<Professor> findAllByProfessorIgrejaId(Long idIgreja);


    @Query("SELECT DISTINCT p FROM Professor p " +
            "JOIN p.professorTurmas pt JOIN pt.turma t " +
            "JOIN t.ebdTurmas et JOIN et.ebd e JOIN e.igrejaEbds ie " +
            "WHERE ie.igreja.id = :igrejaId AND EXTRACT(MONTH FROM p.aniversario) = EXTRACT(MONTH FROM CURRENT_DATE)")
    List<Professor> findProfessoresAniversariantesDoMesByIgrejaId(@Param("igrejaId") Long igrejaId);

    @Query("SELECT DISTINCT p FROM Professor p " +
            "JOIN p.professorTurmas pt JOIN pt.turma t " +
            "JOIN t.ebdTurmas et JOIN et.ebd e JOIN e.igrejaEbds ie " +
            "WHERE ie.igreja.id = :igrejaId AND " +
            "CASE " +
            "WHEN EXTRACT(MONTH FROM p.aniversario) IN (1, 2, 3) THEN 1 " +
            "WHEN EXTRACT(MONTH FROM p.aniversario) IN (4, 5, 6) THEN 2 " +
            "WHEN EXTRACT(MONTH FROM p.aniversario) IN (7, 8, 9) THEN 3 " +
            "WHEN EXTRACT(MONTH FROM p.aniversario) IN (10, 11, 12) THEN 4 " +
            "END = " +
            "CASE " +
            "WHEN EXTRACT(MONTH FROM CURRENT_DATE) IN (1, 2, 3) THEN 1 " +
            "WHEN EXTRACT(MONTH FROM CURRENT_DATE) IN (4, 5, 6) THEN 2 " +
            "WHEN EXTRACT(MONTH FROM CURRENT_DATE) IN (7, 8, 9) THEN 3 " +
            "WHEN EXTRACT(MONTH FROM CURRENT_DATE) IN (10, 11, 12) THEN 4 " +
            "END")
    List<Professor> findProfessoresAniversariantesDoTrimestreByIgrejaId(@Param("igrejaId") Long igrejaId);








    Optional<Professor> findByNome(String nome);

}
