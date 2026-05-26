package stonetek.com.ebdsoft.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class EbdRequest {

    private Long id;

    private String nome;
    
    private String coordenador;
	
    private String viceCoordenador;
	
    private String presbitero;
}
