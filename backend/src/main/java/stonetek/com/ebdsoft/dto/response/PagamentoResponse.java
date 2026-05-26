package stonetek.com.ebdsoft.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class PagamentoResponse {

    private Long id;

    private BigDecimal valorTotal;

    private Integer parcelas;

    private String status;  // Pode ser 'PENDENTE', 'QUITADO', 'ATRASADO'

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Set<ParcelaResponse> parcelasSet = new HashSet<>();
}
