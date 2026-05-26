package stonetek.com.ebdsoft.dto.mapper;

import org.modelmapper.ModelMapper;
import stonetek.com.ebdsoft.dto.dtosespecificos.ProfessorAulaDTO;
import stonetek.com.ebdsoft.dto.request.ProfessorAulaRequest;
import stonetek.com.ebdsoft.dto.request.ProfessorRequest;
import stonetek.com.ebdsoft.dto.response.ProfessorAulaResponse;
import stonetek.com.ebdsoft.dto.response.ProfessorResponse;
import stonetek.com.ebdsoft.model.Professor;
import stonetek.com.ebdsoft.model.ProfessorAula;

import java.util.List;
import java.util.stream.Collectors;

public class ProfessorAulaMapper {

    private final static ModelMapper mapper = new ModelMapper();

    public static ProfessorAulaResponse converter(ProfessorAula professorAula) {
        return mapper.map(professorAula, ProfessorAulaResponse.class);
    }

    public static ProfessorAula converter(ProfessorAulaRequest request) {
        return mapper.map(request, ProfessorAula.class);
    }

    public static ProfessorAula converter(ProfessorAulaResponse response) {
        return mapper.map(response, ProfessorAula.class);
    }

    public static List<ProfessorAulaResponse> converter(List<ProfessorAula> professors) {
        return professors.stream().map(ProfessorAulaMapper::converter).collect(Collectors.toList());
    }

    public static void copyToProperties(ProfessorAulaRequest request, ProfessorAula professorAula) {
        mapper.map(request, professorAula);
    }

    //persolazados
    public static ProfessorAulaDTO toDTO(ProfessorAula professorAula) {
        ProfessorAulaDTO dto = new ProfessorAulaDTO();
        dto.setId(professorAula.getId());
        dto.setIdProfessor(professorAula.getProfessor().getId());
        dto.setAulas(professorAula.getProfessor().getProfessorAulas().stream()
                .map(pa -> AulaMapper.toDTO(pa.getAula()))
                .collect(Collectors.toList()));
        return dto;
    }
}
