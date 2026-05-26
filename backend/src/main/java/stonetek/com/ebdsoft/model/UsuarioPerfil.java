package stonetek.com.ebdsoft.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "usuario_perfil")
public class UsuarioPerfil implements Serializable {

    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario")
    @JsonBackReference("usuario-usuarioPerfil")
    private Usuario usuario;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_perfil")
    @JsonBackReference("perfil-usuarioPerfil")
    private Perfil perfil;

}


