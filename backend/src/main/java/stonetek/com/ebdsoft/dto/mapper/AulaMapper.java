package stonetek.com.ebdsoft.dto.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import stonetek.com.ebdsoft.dto.dtosespecificos.AulaDTO;
import stonetek.com.ebdsoft.dto.request.AulaRequest;
import stonetek.com.ebdsoft.dto.response.*;
import stonetek.com.ebdsoft.model.*;

public class AulaMapper {

    private final static ModelMapper mapper = new ModelMapper();
    
	public static AulaResponse converter(Aula aula) {
        return mapper.map(aula, AulaResponse.class);
    }
	
	public static Aula converter(AulaRequest request) {
        return mapper.map(request, Aula.class);
    }
	
	public static Aula converter(AulaResponse response) {
        return mapper.map(response, Aula.class);
    }
	
	public static List<AulaResponse> converter(List<Aula> aulas) {
        return aulas.stream().map(AulaMapper::converter).collect(Collectors.toList());
    }
	
	public static void copyToProperties(AulaRequest request, Aula aula) {
        mapper.map(request, aula);
    }

    //Métodos personalizados

    public static AulaResponse converte(Aula aula) {
        AulaResponse aulaResponse = new AulaResponse();
        aulaResponse.setId(aula.getId());
        aulaResponse.setLicao(aula.getLicao());
        aulaResponse.setDia(aula.getDia());
        aulaResponse.setAlunosMatriculados(aula.getAlunosMatriculados());
        aulaResponse.setTrimestre(aula.getTrimestre());
        aulaResponse.setAusentes(aula.getAusentes());
        aulaResponse.setPresentes(aula.getPresentes());
        aulaResponse.setVisitantes(aula.getVisitantes());
        aulaResponse.setTotalAssistencia(aula.getTotalAssistencia());
        aulaResponse.setBiblias(aula.getBiblias());
        aulaResponse.setRevistas(aula.getRevistas());
        aulaResponse.setOferta(aula.getOferta());

        Set<ProfessorAula> professorAulas = aula.getProfessorAulas();
        Set<ProfessorAulaResponse> professorAulaResponses = new HashSet<>();
        for (ProfessorAula professorAula : professorAulas) {
            Professor professor = professorAula.getProfessor();
            ProfessorResponse professorResponse = new ProfessorResponse();
            professorResponse.setId(professorResponse.getId());
            professorResponse.setId(professor.getId());
            professorResponse.setNome(professor.getNome());
            professorResponse.setAniversario(professor.getAniversario());
            professorResponse.setIdAula(aula.getId());
            professorResponse.setLicao(aula.getLicao());
            ProfessorAulaResponse professorAulaResponse = new ProfessorAulaResponse();
            professorAulaResponse.setId(professorAula.getId());
            professorAulaResponse.setIdProfessor(professor.getId());
            professorAulaResponse.setNomeProfessor(String.valueOf(professorResponse.getNome()));
            professorAulaResponse.setAniversario(professor.getAniversario());
            professorAulaResponse.setIdAula(aula.getId());
            professorAulaResponse.setLicao(aula.getLicao());
            professorAulaResponses.add(professorAulaResponse);
        }
        aulaResponse.setProfessorAulas(professorAulaResponses);

        Set<AlunoAula> alunoAulas = aula.getAlunoAulas();
        Set<AlunoAulaResponse> alunoAulaResponses = new HashSet<>();
        for (AlunoAula alunoAula : alunoAulas) {
            Aluno aluno = alunoAula.getAluno();
            AlunoResponse alunoResponse = new AlunoResponse();
            alunoResponse.setId(alunoResponse.getId());
            alunoResponse.setId(aluno.getId());
            alunoResponse.setNome(aluno.getNome());
            AlunoAulaResponse alunoAulaResponse = new AlunoAulaResponse();
            alunoAulaResponse.setId(alunoAula.getId());
            alunoAulaResponse.setIdAluno(aluno.getId());
            alunoAulaResponse.setNomeAluno(aluno.getNome());
            alunoAulaResponse.setPresente(alunoAula.getPresente());
            alunoAulaResponse.setIdAula(aula.getId());
            alunoAulaResponse.setLicao(aula.getLicao());
            alunoAulaResponses.add(alunoAulaResponse);
        }

        aulaResponse.setAlunoAulas(alunoAulaResponses);

        Set<AulaTurma> aulasTurmas = aula.getAulaTurmas();
        Set<AulaTurmaResponse> aulaTurmaResponses = new HashSet<>();
        for (AulaTurma aulaTurma : aulasTurmas) {
            AulaTurmaResponse aulaTurmaResponse = new AulaTurmaResponse();
            aulaTurmaResponse.setId(aulaTurma.getId());
            aulaTurmaResponse.setLicao(aula.getLicao());
            aulaTurmaResponse.setIdTurma(aulaTurma.getTurma().getId());
            aulaTurmaResponse.setIdAula(aula.getId());
            aulaTurmaResponse.setNomeTurma(aulaTurma.getTurma().getNome());

            aulaTurmaResponses.add(aulaTurmaResponse);
        }
        aulaResponse.setAulasTurmas(aulaTurmaResponses);

        return aulaResponse;
    }

    public static AulaDTO toDTO(Aula aula) {
        AulaDTO dto = new AulaDTO();
        dto.setId(aula.getId());
        dto.setLicao(aula.getLicao());
        dto.setDia(aula.getDia());
        dto.setAlunosMatriculados(aula.getAlunosMatriculados());
        dto.setTrimestre(Integer.valueOf(aula.getTrimestre()));
        dto.setAusentes(aula.getAusentes());
        dto.setPresentes(aula.getPresentes());
        dto.setVisitantes(aula.getVisitantes());
        dto.setTotalAssistencia(aula.getTotalAssistencia());
        dto.setBiblias(aula.getBiblias());
        dto.setRevistas(aula.getRevistas());
        dto.setOferta(aula.getOferta());
        dto.setMes(aula.getMonth(aula.getDia()));
        dto.setTrimestre(aula.getTrimestre(aula.getDia()));
        return dto;
    }

    public static AulaDTO toDto(Aula aula) {
        return mapper.map(aula, AulaDTO.class);
    }

}
