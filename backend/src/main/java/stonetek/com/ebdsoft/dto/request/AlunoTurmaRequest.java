package stonetek.com.ebdsoft.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class AlunoTurmaRequest {

    private Long id;

    private Long alunoId;

    private Long turmaId;

    private String alunoNome;

    private String turmaNome;

    @JsonProperty("alunoIds")
    private List<Long> alunoIds;

}
