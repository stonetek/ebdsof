package stonetek.com.ebdsoft.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "aluno_aula")
public class AlunoAula implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_aluno")
    @JsonBackReference("aluno-alunoAula")
    private Aluno aluno;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_aula")
    @JsonBackReference("aula-alunoAula")
    private Aula aula;

    @Column(name = "nome_aluno")
    private String nomeAluno;

    private Boolean presente;

    public Boolean isPresente() {
        return this.presente;
    }

    public void setPresente(Boolean presente) {
        this.presente = presente;
    }
}
