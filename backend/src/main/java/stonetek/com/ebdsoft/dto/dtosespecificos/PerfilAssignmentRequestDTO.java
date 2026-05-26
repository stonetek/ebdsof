package stonetek.com.ebdsoft.dto.dtosespecificos;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PerfilAssignmentRequestDTO {

    private String tipoEntidade;

    private Long entidadeId;

    private Long perfilId;
}
