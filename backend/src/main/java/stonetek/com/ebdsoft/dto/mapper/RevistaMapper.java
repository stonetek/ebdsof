package stonetek.com.ebdsoft.dto.mapper;

import org.modelmapper.ModelMapper;
import stonetek.com.ebdsoft.dto.request.AlunoRequest;
import stonetek.com.ebdsoft.dto.request.RevistaRequest;
import stonetek.com.ebdsoft.dto.response.AlunoResponse;
import stonetek.com.ebdsoft.dto.response.RevistaResponse;
import stonetek.com.ebdsoft.model.Aluno;
import stonetek.com.ebdsoft.model.Revista;

import java.util.List;
import java.util.stream.Collectors;

public class RevistaMapper {

    private final static ModelMapper mapper = new ModelMapper();

    public static RevistaResponse converter(Revista revista) {
        return mapper.map(revista, RevistaResponse.class);
    }

    public static Revista converter(RevistaRequest request) {
        return mapper.map(request, Revista.class);
    }

    public static Revista converter(RevistaResponse response) {
        return mapper.map(response, Revista.class);
    }

    public static List<RevistaResponse> converter(List<Revista> revistas) {
        return revistas.stream().map(RevistaMapper::converter).collect(Collectors.toList());
    }

    public static void copyToProperties(RevistaRequest request, Revista revista) {
        mapper.map(request, revista);
    }
}
