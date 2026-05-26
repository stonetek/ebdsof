package stonetek.com.ebdsoft.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ProfessorAulaRequest {

    private Long id;

    private Long idProfessor;

    private String nomeProfessor;

    private LocalDate aniversario;

    private Long idAula;

    private String licao;

}
