package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class EbdDTO {

    private Long id;

    private String nome;
    
    private String coordenador;
	
    private String viceCoordenador;
	
    private String presbitero;

}
