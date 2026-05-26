package stonetek.com.ebdsoft.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UsuarioPerfilResponse {

    private Long id;

    private Long idUsuario;

    private String nomeUsuario;

    private Long idPerfil;

    private String nomePerfil;
}
