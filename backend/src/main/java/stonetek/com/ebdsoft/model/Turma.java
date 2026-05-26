package stonetek.com.ebdsoft.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "turma")
public class Turma implements Serializable {

    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String nome;

    private Integer idadeMinima;

    private Integer idadeMaxima;

    @OneToMany(mappedBy = "turma", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("turma-professorTurmas")
    private Set<ProfessorTurma> professorTurmas = new HashSet<>();

    @OneToMany(mappedBy = "turma", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("turma-alunoTurma")
    private Set<AlunoTurma> alunoTurmas = new HashSet<>();

    @OneToMany(mappedBy = "turma", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("turma-aulaTurmas")
    private Set<AulaTurma> aulaTurmas = new HashSet<>();

    @OneToMany(mappedBy = "turma", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("turma-ebdTurma")
    private Set<EbdTurma> ebdTurmas = new HashSet<>();
}
