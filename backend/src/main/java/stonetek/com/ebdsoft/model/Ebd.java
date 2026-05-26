package stonetek.com.ebdsoft.model;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "ebd")
public class Ebd implements Serializable {

    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String nome;

    private String coordenador;

    private String viceCoordenador;

    private String presbitero;

    @OneToMany(mappedBy = "ebd", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("ebd-igrejaEbd")
    private Set<IgrejaEbd> igrejaEbds = new HashSet<>();

    @OneToMany(mappedBy = "ebd", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("ebd-ebdTurma")
    private Set<EbdTurma> ebdTurmas = new HashSet<>();

}
