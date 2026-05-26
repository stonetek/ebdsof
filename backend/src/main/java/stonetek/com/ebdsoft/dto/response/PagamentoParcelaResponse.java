package stonetek.com.ebdsoft.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PagamentoParcelaResponse {

    private Long id;

    private Long pagamentoId;

    private Long parcelaId;
}
