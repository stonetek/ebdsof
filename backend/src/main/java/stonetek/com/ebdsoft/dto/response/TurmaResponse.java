package stonetek.com.ebdsoft.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class TurmaResponse {

    private Long id;

    private String nome;

    private Integer idadeMinima;

    private Integer idadeMaxima;

    private List<EbdResponse> ebdResponses;

    public TurmaResponse(Long id, String nome) {
    }
}
