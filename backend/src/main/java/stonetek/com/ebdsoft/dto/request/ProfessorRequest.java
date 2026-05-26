package stonetek.com.ebdsoft.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class ProfessorRequest {

    private Long id;

    private String nome;

    private LocalDate aniversario;

    private Long igrejaId;

    private Long turmaId;

}
