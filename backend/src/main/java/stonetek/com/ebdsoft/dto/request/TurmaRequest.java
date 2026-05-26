package stonetek.com.ebdsoft.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TurmaRequest {

    private Long id;

    private String nome;

    private Integer idadeMinima;

    private Integer idadeMaxima;

    private Long igrejaId;

}
