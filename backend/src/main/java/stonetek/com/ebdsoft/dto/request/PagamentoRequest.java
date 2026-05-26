package stonetek.com.ebdsoft.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PagamentoRequest {

    private Long id;

    private BigDecimal valorTotal;

    private Integer parcelas;

    private String status;  // Pode ser 'PENDENTE', 'QUITADO', 'ATRASADO'

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
