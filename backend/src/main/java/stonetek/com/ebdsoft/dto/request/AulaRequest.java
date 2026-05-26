package stonetek.com.ebdsoft.dto.request;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stonetek.com.ebdsoft.dto.response.AulaTurmaResponse;
import stonetek.com.ebdsoft.dto.response.ProfessorResponse;
import stonetek.com.ebdsoft.model.AulaTurma;


@Setter
@Getter
@NoArgsConstructor
public class AulaRequest {

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

    private Set<ProfessorAulaRequest> professorAulas;

    private Set<AlunoAulaRequest> alunoAulas;

    private Set<AulaTurmaRequest> aulaTurmas;

    //private Set<AulaTurmaResponse> aulasTurmas;

}
