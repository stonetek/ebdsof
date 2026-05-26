package stonetek.com.ebdsoft.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import stonetek.com.ebdsoft.util.Perfilable;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "professor")
public class Professor implements Serializable, Perfilable {

    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String nome;

    private LocalDate aniversario;

    @JsonIgnore
    @OneToMany(mappedBy = "professor", fetch = FetchType.EAGER)
    @JsonManagedReference("professor-professorAulas")
    private Set<ProfessorAula> professorAulas;

    @JsonIgnore
    @OneToMany(mappedBy = "professor", fetch = FetchType.EAGER)
    @JsonManagedReference("professor-professorTurmas")
    private Set<ProfessorTurma> professorTurmas;

    @JsonIgnore
    @OneToMany(mappedBy = "professor", fetch = FetchType.EAGER)
    @JsonManagedReference("professor-pagamentoRevista")
    private Set<PagamentoRevista> pagamentoRevistas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "perfil_id")
    private Perfil perfil;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Override
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
    
}
