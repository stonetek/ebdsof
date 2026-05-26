package stonetek.com.ebdsoft.dto.dtosespecificos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TurmasDTO {

    private String nomeTurma;

    private Integer totalMatriculados;

    private Integer totalPresentes;

    private Integer totalAusentes;

    private Integer totalVisitantes;

    private Integer totalAssistencia;

    private Integer totalBiblias;

    private Integer totalRevistas;

    private Double totalOferta;
}
