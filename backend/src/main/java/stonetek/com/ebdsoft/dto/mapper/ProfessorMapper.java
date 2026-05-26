package stonetek.com.ebdsoft.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import stonetek.com.ebdsoft.dto.dtosespecificos.ProfessorDTO;
import stonetek.com.ebdsoft.dto.request.ProfessorRequest;
import stonetek.com.ebdsoft.dto.response.ProfessorResponse;
import stonetek.com.ebdsoft.model.Professor;

public class ProfessorMapper {

    private final static ModelMapper mapper = new ModelMapper();
    
	public static ProfessorResponse converter(Professor professor) {
        return mapper.map(professor, ProfessorResponse.class);
    }
	
	public static Professor converter(ProfessorRequest request) {
        return mapper.map(request, Professor.class);
    }
	
	public static Professor converter(ProfessorResponse response) {
        return mapper.map(response, Professor.class);
    }
	
	public static List<ProfessorResponse> converter(List<Professor> professors) {
        return professors.stream().map(ProfessorMapper::converter).collect(Collectors.toList());
    }
	
	public static void copyToProperties(ProfessorRequest request, Professor professor) {
        mapper.map(request, professor);
    }

    //personalizados

    public static ProfessorDTO toDTO(Professor professor) {
        ProfessorDTO dto = new ProfessorDTO();
        dto.setId(professor.getId());
        dto.setNome(professor.getNome());
        dto.setProfessorAulas(professor.getProfessorAulas().stream().map(ProfessorAulaMapper::toDTO).collect(Collectors.toList()));
        return dto;
    }
}
