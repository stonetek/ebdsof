package stonetek.com.ebdsoft.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import stonetek.com.ebdsoft.util.Area;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "igreja")
public class Igreja implements Serializable {

    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String nome;

	private String endereco;

	private String complemento;

	private String cnpj;
    
	private String cep;

    private String bairro;

    private String cidade;

    @JsonIgnore
    @OneToMany(mappedBy = "igreja", fetch = FetchType.EAGER)
    @JsonManagedReference("igreja-igrejaEbd")
    private Set<IgrejaEbd> igrejaEbds;

    @Enumerated(EnumType.STRING)
    private Area area;
}
