package stonetek.com.ebdsoft.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class IgrejaEbdResponse {

    private Long id;

    private Long idEbd;

    private Long idIgreja;

    private String nomeIgreja;

    private String nomeEbd;

}
