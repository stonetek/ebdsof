package stonetek.com.ebdsoft.dto.dtosespecificos;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stonetek.com.ebdsoft.dto.response.ProfessorResponse;
import stonetek.com.ebdsoft.model.Aluno;
import stonetek.com.ebdsoft.model.Aula;
import stonetek.com.ebdsoft.model.Professor;
import stonetek.com.ebdsoft.model.Turma;


@Setter
@Getter
@NoArgsConstructor
public class AulaDTO {

    private Long id;

    private String licao;

    private LocalDate dia;

    private String alunosMatriculados;

    private Integer trimestre;

    private String ausentes;

    private String presentes;

    private String visitantes;

    private String totalAssistencia;

    private String biblias;

    private String revistas;

    private Double oferta;

    private ProfessorResponse professor;

    private Aula aula;

    private List<Aluno> alunos;

    private List<Professor> professores;

    private List<Turma> turmas;

    private Integer mes;



    public AulaDTO(Aula aula, List<Aluno> alunos, List<Professor> professores, List<Turma> turmas) {
        this.aula = aula;
        this.alunos = alunos;
        this.professores = professores;
        this.turmas = turmas;
        this.dia = dia;
    }

}
