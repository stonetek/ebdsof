package stonetek.com.ebdsoft.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stonetek.com.ebdsoft.dto.dtosespecificos.TurmasDTO;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class RelatorioRequestDTO {

    private LocalDate data;

    private Long idEbd;

    private Integer mes;

    private Integer trimestre;

    private Integer ano;

    //private List<TurmasDTO> turmas;
}
