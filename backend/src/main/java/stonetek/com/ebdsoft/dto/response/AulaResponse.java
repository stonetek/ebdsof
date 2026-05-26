package stonetek.com.ebdsoft.dto.response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stonetek.com.ebdsoft.dto.request.AulaTurmaRequest;
import stonetek.com.ebdsoft.model.Aula;
import stonetek.com.ebdsoft.model.Turma;

@Setter
@Getter
@NoArgsConstructor
public class AulaResponse {

    private Long id;
    
    private String licao;

    private LocalDate dia;

    private String alunosMatriculados;
	
    private String trimestre;
	
    private String ausentes;
	
    private String presentes;
	
    private String visitantes;
	
    private String totalAssistencia;
	
    private String biblias;
	
    private String revistas;
	
    private Double oferta;

    private Set<ProfessorAulaResponse> professorAulas;

    private Set<AlunoAulaResponse> alunoAulas;

    //private Set<AulaTurmaRequest> aulaTurmas;

    private Set<AulaTurmaResponse> aulasTurmas;

    private List<AlunoResponse> alunosPorTurma;

    private List<Turma> turmas;

    public AulaResponse(Aula aula, List<Turma> turmas, List<AlunoResponse> alunosPorTurma) {
        this.id = aula.getId();
        this.licao = aula.getLicao();
        this.dia = aula.getDia();
        this.alunosMatriculados = aula.getAlunosMatriculados();
        this.trimestre = aula.getTrimestre();
        this.ausentes = aula.getAusentes();
        this.presentes = aula.getPresentes();
        this.visitantes = aula.getVisitantes();
        this.totalAssistencia = aula.getTotalAssistencia();
        this.biblias = aula.getBiblias();
        this.revistas = aula.getRevistas();
        this.oferta = aula.getOferta();
        this.professorAulas = aula.getProfessorAulas().stream()
                .map(ProfessorAulaResponse::new)
                .collect(Collectors.toSet());
        this.alunoAulas = aula.getAlunoAulas().stream()
                .map(AlunoAulaResponse::new)
                .collect(Collectors.toSet());
        this.aulasTurmas = aula.getAulaTurmas().stream()
                .map(AulaTurmaResponse::new)
                .collect(Collectors.toSet());
        this.turmas = turmas;
        this.alunosPorTurma = alunosPorTurma;
    }

    public AulaResponse(Aula aula, List<AlunoResponse> alunosPorTurma) {
        this.id = aula.getId();
        this.licao = aula.getLicao();
        this.dia = aula.getDia();
        this.alunosMatriculados = aula.getAlunosMatriculados();
        this.trimestre = aula.getTrimestre();
        this.ausentes = aula.getAusentes();
        this.presentes = aula.getPresentes();
        this.visitantes = aula.getVisitantes();
        this.totalAssistencia = aula.getTotalAssistencia();
        this.biblias = aula.getBiblias();
        this.revistas = aula.getRevistas();
        this.oferta = aula.getOferta();
        this.professorAulas = aula.getProfessorAulas().stream()
                .map(ProfessorAulaResponse::new)
                .collect(Collectors.toSet());
        this.alunoAulas = aula.getAlunoAulas().stream()
                .map(AlunoAulaResponse::new)
                .collect(Collectors.toSet());
        this.aulasTurmas = aula.getAulaTurmas().stream()
                .map(AulaTurmaResponse::new)
                .collect(Collectors.toSet());
        //this.turmas = turmas;
        this.alunosPorTurma = alunosPorTurma;
    }
}

