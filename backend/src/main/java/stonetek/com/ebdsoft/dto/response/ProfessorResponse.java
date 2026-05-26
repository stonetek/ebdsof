package stonetek.com.ebdsoft.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class ProfessorResponse {

    private Long id;

    private String nome;

    private LocalDate aniversario;

    private Long idProfessor;

    private Long idAula;

    private String licao;

}
