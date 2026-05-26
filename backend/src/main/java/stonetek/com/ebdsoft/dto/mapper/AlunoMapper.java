package stonetek.com.ebdsoft.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import stonetek.com.ebdsoft.dto.dtosespecificos.AlunoDTO;
import stonetek.com.ebdsoft.dto.request.AlunoRequest;
import stonetek.com.ebdsoft.dto.response.AlunoResponse;
import stonetek.com.ebdsoft.model.Aluno;

public class AlunoMapper {

    private final static ModelMapper mapper = new ModelMapper();
    
	public static AlunoResponse converter(Aluno aluno) {
        return mapper.map(aluno, AlunoResponse.class);
    }
	
	public static Aluno converter(AlunoRequest request) {
        return mapper.map(request, Aluno.class);
    }
	
	public static Aluno converter(AlunoResponse response) {
        return mapper.map(response, Aluno.class);
    }
	
	public static List<AlunoResponse> converter(List<Aluno> alunos) {
        return alunos.stream().map(AlunoMapper::converter).collect(Collectors.toList());
    }
	
	public static void copyToProperties(AlunoRequest request, Aluno aluno) {
        mapper.map(request, aluno);
    }

    public static AlunoDTO toDto(Aluno aluno) {
        AlunoDTO dto = mapper.map(aluno, AlunoDTO.class);
        return dto;
    }

}
