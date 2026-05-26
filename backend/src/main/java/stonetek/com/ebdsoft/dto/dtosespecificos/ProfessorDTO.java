package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ProfessorDTO {

    private Long id;

    private String nome;

    private LocalDate aniversario;

    private List<ProfessorAulaDTO> professorAulas;

}
