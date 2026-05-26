package stonetek.com.ebdsoft.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import stonetek.com.ebdsoft.dto.dtosespecificos.AlunoDTO;
import stonetek.com.ebdsoft.dto.dtosespecificos.ProfessorDTO;
import stonetek.com.ebdsoft.dto.dtosespecificos.TurmaDTO;
import stonetek.com.ebdsoft.dto.request.TurmaRequest;
import stonetek.com.ebdsoft.dto.response.TurmaResponse;
import stonetek.com.ebdsoft.model.Turma;

public class TurmaMapper {

    private final static ModelMapper mapper = new ModelMapper();
    
	public static TurmaResponse converter(Turma turma) {
        return mapper.map(turma, TurmaResponse.class);
    }
	
	public static Turma converter(TurmaRequest request) {
        return mapper.map(request, Turma.class);
    }
	
	public static Turma converter(TurmaResponse response) {
        return mapper.map(response, Turma.class);
    }
	
	public static List<TurmaResponse> converter(List<Turma> turmas) {
        return turmas.stream().map(TurmaMapper::converter).collect(Collectors.toList());
    }
	
	public static void copyToProperties(TurmaRequest request, Turma turma) {
        mapper.map(request, turma);
    }

    public static TurmaDTO toDto(Turma turma) {
        TurmaDTO dto = mapper.map(turma, TurmaDTO.class);

        // Mapear alunos a partir de alunoTurmas
        List<AlunoDTO> alunoDTOs = turma.getAlunoTurmas().stream()
                .map(alunoTurma -> mapper.map(alunoTurma.getAluno(), AlunoDTO.class))
                .collect(Collectors.toList());
        dto.setAlunos(alunoDTOs);

        // Mapear professores a partir de professorTurmas
        List<ProfessorDTO> professorDTOs = turma.getProfessorTurmas().stream()
                .map(professorTurma -> mapper.map(professorTurma.getProfessor(), ProfessorDTO.class))
                .collect(Collectors.toList());
        dto.setProfessores(professorDTOs);

        return dto;
    }

}
