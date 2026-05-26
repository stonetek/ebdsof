package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProfessorAulaDTO {

    private Long id;

    private Long idAula;

    private Long idProfessor;

    private String nomeProfessor;

    private String licao;

    private Date dia;

    private String alunosMatriculados;

    private String trimestre;

    private String ausentes;

    private String presentes;

    private String visitantes;

    private String totalAssistencia;

    private String biblias;

    private String revistas;

    private Double oferta;

    private List<AulaDTO> aulas;

    public ProfessorAulaDTO(Long id, String nomeProfessor, String licao) {
        this.id = id;
        this.nomeProfessor = nomeProfessor;
        this.licao = licao;
    }
}
