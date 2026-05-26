package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class RelatorioDTO {

    private Integer totalMatriculados = 0;
    private Integer totalPresentes = 0;
    private Integer totalAusentes = 0;
    private Integer totalVisitantes = 0;
    private Integer totalAssistencia = 0;
    private Integer totalBiblias = 0;
    private Integer totalRevistas = 0;
    private Double totalOferta = 0.0;
    private List<TurmasDTO> turmas;
}
