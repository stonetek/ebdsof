package stonetek.com.ebdsoft.dto.mapper;

import org.modelmapper.ModelMapper;
import stonetek.com.ebdsoft.dto.request.PerfilRequest;
import stonetek.com.ebdsoft.dto.response.PerfilResponse;
import stonetek.com.ebdsoft.model.Perfil;

import java.util.List;
import java.util.stream.Collectors;

public class PerfilMapper {

    private final static ModelMapper mapper = new ModelMapper();

    public static PerfilResponse converter(Perfil perfil) {
        return mapper.map(perfil, PerfilResponse.class);
    }

    public static Perfil converter(PerfilRequest request) {
        return mapper.map(request, Perfil.class);
    }

    public static Perfil converter(PerfilResponse response) {
        return mapper.map(response, Perfil.class);
    }

    public static List<PerfilResponse> converter(List<Perfil> perfis) {
        return perfis.stream().map(PerfilMapper::converter).collect(Collectors.toList());
    }

    public static void copyToProperties(PerfilRequest request, Perfil perfil) {
        mapper.map(request, perfil);
    }
}
