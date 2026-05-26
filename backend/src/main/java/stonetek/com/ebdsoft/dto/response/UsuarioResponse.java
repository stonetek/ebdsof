package stonetek.com.ebdsoft.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class UsuarioResponse {

    private Long id;

    private String username;

    private String nome;

    private String password;

    private Boolean status;

    private List<String> perfis = new ArrayList<>();

    private Long igrejaId;
}
