package stonetek.com.ebdsoft.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class IgrejaEbdRequest {

    private Long id;

    private Long idEbd;

    private Long idIgreja;

    private String nomeIgreja;

    private String nomeEbd;

}
