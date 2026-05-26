package stonetek.com.ebdsoft.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class AlunoRequest {

    private Long id;

    private String nome;

    private LocalDate aniversario;

    private Boolean presente;

    private String area;

    private Boolean novoConvertido;

    private Character sexo;

    private Long igrejaId;
    
}
