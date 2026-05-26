package stonetek.com.ebdsoft.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RevistaRequest {

    private Long id;

    private String nome;

    private String formato;

    private String tipo;

    private Double preco;

    private Integer quantidade;


}
