package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stonetek.com.ebdsoft.model.Aluno;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class AlunoDTO {

    private Long id;

    private String nome;

    private LocalDate aniversario;

    private List<TurmaDTO> turmas;

    private List<AulaDTO> aulas;

    private String perfilNome;

    private String area;

    private Character sexo;

    private Boolean novoConvertido;

    private Boolean matriculado;

    public AlunoDTO(Aluno aluno, boolean matriculado) {
        this.id = aluno.getId();
        this.nome = aluno.getNome();
        this.perfilNome = aluno.getPerfil().getNome();
        this.area = String.valueOf(aluno.getArea());
        this.aniversario = aluno.getAniversario();
        this.perfilNome = String.valueOf(aluno.getPerfil());
        this.sexo = aluno.getSexo();
        this.novoConvertido = aluno.isNovoConvertido();
        this.matriculado = matriculado;
    }
}
