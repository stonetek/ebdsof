package stonetek.com.ebdsoft.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UsuarioPerfilRequest {

    private Long id;

    private Long idUsuario;

    private String nomeUsuario;

    private Long idPerfil;

    private String nomePerfil;
}
