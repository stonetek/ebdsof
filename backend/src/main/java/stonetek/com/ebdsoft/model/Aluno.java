package stonetek.com.ebdsoft.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import stonetek.com.ebdsoft.util.Area;
import stonetek.com.ebdsoft.util.Perfilable;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "aluno")
public class Aluno implements Serializable, Perfilable {

    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String nome;

    private LocalDate aniversario;

    private Boolean novoConvertido;

    private Character sexo;

    //private Boolean presente;

    @JsonIgnore
    @OneToMany(mappedBy = "aluno", fetch = FetchType.EAGER)
    @JsonManagedReference("aluno-alunoAula")
    private Set<AlunoAula> alunoAulas;

    @JsonIgnore
    @OneToMany(mappedBy = "aluno", fetch = FetchType.EAGER)
    @JsonManagedReference("aluno-alunoTurma")
    private Set<AlunoTurma> alunoTurmas;

    @JsonIgnore
    @OneToMany(mappedBy = "aluno", fetch = FetchType.EAGER)
    @JsonManagedReference("aluno-pagamentoRevista")
    private Set<PagamentoRevista> pagamentoRevistas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "perfil_id")
    private Perfil perfil;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private Area area;


    @Override
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Boolean isNovoConvertido() {
        return novoConvertido;
    }

    public void setNovoConvertido(Boolean presente) {
        this.novoConvertido = novoConvertido;
    }

}
