package stonetek.com.ebdsoft.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "perfil")
public class Perfil implements Serializable {

    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String nome;

    @OneToMany(mappedBy = "perfil", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("perfil-usuarioPerfil")
    Set<UsuarioPerfil> usuarioPerfis = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "perfil", fetch = FetchType.LAZY)
    private Set<Aluno> alunos = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "perfil", fetch = FetchType.LAZY)
    private Set<Professor> professores = new HashSet<>();
}
