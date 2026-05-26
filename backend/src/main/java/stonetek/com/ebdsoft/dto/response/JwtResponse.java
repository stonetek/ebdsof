package stonetek.com.ebdsoft.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtResponse {

    private String jwt;

    private String userProfile;

    private Long usuarioId;

    private Long perfilId;

    private String nomePerfil;

    private Long classeId;

    private Long igrejaId;

    private String nomeUsuario;


    public JwtResponse(String jwt, String userProfile, Long usuarioId,
                       Long perfilId, String nomePerfil, Long classeId, Long igrejaId, String nomeUsuario) {
        this.jwt = jwt;
        this.userProfile = userProfile;
        this.usuarioId = usuarioId;
        this.perfilId = perfilId;
        this.classeId = classeId;
        this.igrejaId = igrejaId;
        this.nomeUsuario = nomeUsuario;
        this.nomePerfil = nomePerfil;
    }

}


