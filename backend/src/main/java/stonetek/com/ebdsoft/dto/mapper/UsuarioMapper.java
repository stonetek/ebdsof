package stonetek.com.ebdsoft.dto.mapper;

import org.modelmapper.ModelMapper;
import stonetek.com.ebdsoft.dto.request.UsuarioRequest;
import stonetek.com.ebdsoft.dto.response.UsuarioResponse;
import stonetek.com.ebdsoft.model.Perfil;
import stonetek.com.ebdsoft.model.Usuario;
import stonetek.com.ebdsoft.model.UsuarioPerfil;
import stonetek.com.ebdsoft.repository.PerfilRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class UsuarioMapper {

    private final static ModelMapper mapper = new ModelMapper();


    public static UsuarioResponse converter(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setNome(usuario.getNome());
        response.setUsername(usuario.getUsername());
        response.setStatus(usuario.getStatus());
        response.setIgrejaId(usuario.getIgreja() != null ? usuario.getIgreja().getId() : null);
        response.setPerfis(usuario.getUsuarioPerfis().stream()
                .map(usuarioPerfil -> usuarioPerfil.getPerfil().getNome())
                .collect(Collectors.toList()));
        return response;
    }

    public static Usuario converter(UsuarioRequest request, PerfilRepository perfilRepository) {
        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setNome(request.getNome());
        usuario.setPassword(request.getPassword());
        usuario.setStatus(true); // Definindo valor padrão para status

        Set<UsuarioPerfil> usuarioPerfis = new HashSet<>();
        for (String perfilNome : request.getPerfis()) {
            Perfil perfil = perfilRepository.findByNome(perfilNome)
                    .orElseThrow(() -> new RuntimeException("Perfil não encontrado: " + perfilNome));
            UsuarioPerfil usuarioPerfil = new UsuarioPerfil();
            usuarioPerfil.setUsuario(usuario);
            usuarioPerfil.setPerfil(perfil);
            usuarioPerfis.add(usuarioPerfil);
        }
        usuario.setUsuarioPerfis(usuarioPerfis);
        return usuario;
    }

    public static Usuario converter(UsuarioResponse response) {
        return mapper.map(response, Usuario.class);
    }

    public static List<UsuarioResponse> converter(List<Usuario> usuarios) {
        return usuarios.stream().map(UsuarioMapper::converter).collect(Collectors.toList());
    }

    public static void copyToProperties(UsuarioRequest request, Usuario usuario) {
        usuario.setUsername(request.getUsername());
        //usuario.setPassword(request.getPassword());
        usuario.setStatus(request.getStatus());
    }
}
