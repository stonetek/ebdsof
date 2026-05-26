package stonetek.com.ebdsoft.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stonetek.com.ebdsoft.model.Aluno;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class AlunoResponse {

    private Long id;

    private String nome;

    private LocalDate aniversario;

    private String area;

    private Boolean novoConvertido;

    private Character sexo;

    public AlunoResponse(Long id, String nome, LocalDate aniversario, Boolean novoConvertido, Character sexo) {
        this.id = id;
        this.nome = nome;
        this.aniversario = aniversario;
        this.area = area;
        this.novoConvertido = novoConvertido;
        this.sexo = sexo;
    }

    public AlunoResponse(Aluno aluno) {
        this.id = aluno.getId();
        this.nome = aluno.getNome();
    }
}
