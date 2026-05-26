package stonetek.com.ebdsoft.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import stonetek.com.ebdsoft.dto.dtosespecificos.*;
import stonetek.com.ebdsoft.dto.mapper.EbdMapper;
import stonetek.com.ebdsoft.dto.request.EbdRequest;
import stonetek.com.ebdsoft.dto.response.EbdResponse;
import stonetek.com.ebdsoft.dto.response.EbdTurmaResponse;
import stonetek.com.ebdsoft.exception.EbdNotFoundException;
import stonetek.com.ebdsoft.exception.TurmaAlreadyEnrolledException;
import stonetek.com.ebdsoft.model.*;
import stonetek.com.ebdsoft.repository.*;
import stonetek.com.ebdsoft.util.ResourceUriUtil;

@Service
@RequiredArgsConstructor
public class EbdService {



    private final EbdRepository ebdRepository;

    private final EbdTurmaRepository ebdTurmaRepository;

    private final TurmaRepository turmaRepository;

    private final ProfessorRepository professorRepository;

    private final AlunoRepository alunoRepository;

    private final IgrejaEbdRepository igrejaEbdRepository;

    private final IgrejaRepository igrejaRepository;

    private final AulaRepository aulaRepository;


    public List<EbdResponse> listar() {
        List<Ebd> ebds = ebdRepository.findAllByOrderByNomeAsc();
        return EbdMapper.converter(ebds);
    }

    public EbdResponse salvar(EbdRequest request) {
        Ebd ebd = EbdMapper.converter(request);
        ebd = ebdRepository.save(ebd);
        ResourceUriUtil.addUriInResponseHeader(ebd.getId()); // Adiciona no header da resquição o recurso que foi
                                                                   // criado
        return EbdMapper.converter(ebd);
    }

    public EbdResponse buscarPorId(Long idEbd) {
        Optional<Ebd> ebd = ebdRepository.findById(idEbd);
        if (ebd.isEmpty()) {
            throw new EbdNotFoundException(idEbd);
        }
        return EbdMapper.converter(ebd.get());
    }

    public EbdResponse editar(Long idEbd, EbdRequest request) {
        EbdResponse ebdEncontrado = buscarPorId(idEbd);
        Ebd ebd = EbdMapper.converter(ebdEncontrado);
        EbdMapper.copyToProperties(request, ebd);
        ebd = ebdRepository.save(ebd);
        return EbdMapper.converter(ebd);
    }

    public void excluir(Long idEbd) {
        try {
            ebdRepository.deleteById(idEbd);
        } catch (EmptyResultDataAccessException ex) {
            throw new EbdNotFoundException(idEbd);
        }
    }

    //Métodos personalizados

    public EbdDTO mapEbdToDTO(Ebd ebd) {
        EbdDTO ebdDTO = new EbdDTO();
        ebdDTO.setId(ebd.getId());
        ebdDTO.setNome(ebd.getNome());
        ebdDTO.setCoordenador(ebd.getCoordenador());
        ebdDTO.setViceCoordenador(ebd.getViceCoordenador());
        ebdDTO.setPresbitero(ebd.getPresbitero());
        return ebdDTO;
    }

    public TurmaDTO mapTurmaToDTO(Turma turma) {
        TurmaDTO turmaDTO = new TurmaDTO();
        turmaDTO.setId(turma.getId());
        turmaDTO.setNome(turma.getNome());
        return turmaDTO;
    }
    
    
    
    public List<Long> getTurmaIdsByEbdId(Long ebdId) {
        return ebdTurmaRepository.findTurmaIdsByEbdId(ebdId);
    }

    public List<Long> getTurmaIdsByEbdNome(String nome) {
        Optional<Ebd> ebdOptional = ebdRepository.findByNome(nome);
        if (ebdOptional.isPresent()) {
            Long ebdId = ebdOptional.get().getId();
            return ebdTurmaRepository.findTurmaIdsByEbdId(ebdId);
        } else {
            return List.of();
        }
    }

    public EbdTurma criarVinculoEbdTurma(Long idEbd, Long idTurma) {
        Ebd ebd = ebdRepository.findById(idEbd)
                .orElseThrow(() -> new EntityNotFoundException("Ebd not found"));
        Turma turma = turmaRepository.findById(idTurma)
                .orElseThrow(() -> new EntityNotFoundException("Turma not found"));

        boolean turmaVinculadaOutraEbd = ebdTurmaRepository.existsByTurmaAndEbdNot(turma, ebd);
        if (turmaVinculadaOutraEbd) {
            throw new TurmaAlreadyEnrolledException("A Turma com id: " + idTurma + " já está vinculada outra EBD.");
        }

        boolean exists = ebdTurmaRepository.existsByEbdAndTurma(ebd, turma);
        if (exists) {
            throw new TurmaAlreadyEnrolledException("A Turma com id: " + idTurma
                    + " já está matriculada na EBD com id: " + idEbd);
        }
        EbdTurma ebdTurma = new EbdTurma();
        ebdTurma.setEbd(ebd);
        ebdTurma.setTurma(turma);
        return ebdTurmaRepository.save(ebdTurma);
    }



    public EbdTurma atualizarVinculoEbdTurma(Long id, Long idEbd, Long idTurma) {
        EbdTurma ebdTurma = ebdTurmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EbdTurma not found"));

        Ebd ebd = ebdRepository.findById(idEbd)
                .orElseThrow(() -> new EntityNotFoundException("Ebd not found"));

        Turma turma = turmaRepository.findById(idTurma)
                .orElseThrow(() -> new EntityNotFoundException("Turma not found"));

        ebdTurma.setEbd(ebd);
        ebdTurma.setTurma(turma);

        return ebdTurmaRepository.save(ebdTurma);
    }

    public EbdTurmaResponse findEbdTurmaById(Long id) {
        Optional<EbdTurma> ebdTurmaOptional = ebdTurmaRepository.findById(id);
        if (ebdTurmaOptional.isPresent()) {
            EbdTurma ebdTurma = ebdTurmaOptional.get();
            return mapToResponse(ebdTurma);
        } else {
            throw new ResourceNotFoundException("EbdTurma not found with id: " + id);
        }
    }

    private EbdTurmaResponse mapToResponse(EbdTurma ebdTurma) {
        EbdTurmaResponse response = new EbdTurmaResponse();
        response.setId(ebdTurma.getId());
        response.setEbdId(ebdTurma.getEbd().getId());
        response.setTurmaId(ebdTurma.getTurma().getId());
        response.setNomeTurma(ebdTurma.getTurma().getNome());
        response.setNomeEbd(ebdTurma.getEbd().getNome());
        return response;
    }



    public List<EbdTurma> obterTodasAsRelacoesEbdTurma() {
        return ebdTurmaRepository.findAll();
    }

    public List<EbdTurmaDTO> obterTodasAsRelacoesEbdTurmaComDadosCompletos() {
        List<EbdTurma> ebdTurmas = ebdTurmaRepository.findAll();
        return ebdTurmas.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private EbdTurmaDTO mapToDTO(EbdTurma ebdTurma) {
        Ebd ebd = ebdTurma.getEbd();
        Turma turma = ebdTurma.getTurma();

        if (ebd == null || turma == null) {
            throw new EntityNotFoundException("Ebd or Turma not found in EbdTurma");
        }

        EbdDTO ebdDTO = mapEbdToDTO(ebd);
        TurmaDTO turmaDTO = mapTurmaToDTO(turma);

        EbdTurmaDTO dto = new EbdTurmaDTO();
        dto.setId(ebdTurma.getId());
        dto.setEbd(ebdDTO);
        dto.setTurma(turmaDTO);

        return dto;
    }


    public List<Professor> getProfessoresAniversariantesDoMes() {
        return professorRepository.findProfessoresAniversariantesDoMes();
    }

    public List<Professor> getProfessoresAniversariantesDoTrimestre() {
        return professorRepository.findProfessoresAniversariantesDoTrimestre();
    }

    public List<Aluno> getAlunosAniversariantesDoMes() {
        return alunoRepository.findAlunosAniversariantesDoMes();
    }

    public List<Aluno> getAlunosAniversariantesDoTrimestre() {
        return alunoRepository.findAlunosAniversariantesDoTrimestre();
    }

    public List<Object> getAniversariantesDoMes() {
        return Stream.concat(getProfessoresAniversariantesDoMes().stream(), getAlunosAniversariantesDoMes().stream())
                .collect(Collectors.toList());
    }

    public List<Object> getAniversariantesDoTrimestre() {
        return Stream.concat(getProfessoresAniversariantesDoTrimestre().stream(), getAlunosAniversariantesDoTrimestre().stream())
                .collect(Collectors.toList());
    }

    public List<Object> getAniversariantesDoMesPorIgreja(Long igrejaId) {
        List<Aluno> alunos = alunoRepository.findAlunosAniversariantesDoMesByIgrejaId(igrejaId);
        List<Professor> professores = professorRepository.findProfessoresAniversariantesDoMesByIgrejaId(igrejaId);
        return Stream.concat(professores.stream(), alunos.stream()).collect(Collectors.toList());
    }

    public List<Object> getAniversariantesDoTrimestrePorIgreja(Long igrejaId) {
        List<Aluno> alunos = alunoRepository.findAlunosAniversariantesDoTrimestreByIgrejaId(igrejaId);
        List<Professor> professores = professorRepository.findProfessoresAniversariantesDoTrimestreByIgrejaId(igrejaId);
        return Stream.concat(professores.stream(), alunos.stream()).collect(Collectors.toList());

    }



    @Transactional
    public void vincularIgrejaAEbd(String ebdNome, String igrejaNome) {
        Ebd ebd = ebdRepository.findByNome(ebdNome)
                .orElseThrow(() -> new IllegalArgumentException("Ebd not found with name: " + ebdNome));
        Igreja igreja = igrejaRepository.findByNome(igrejaNome)
                .orElseThrow(() -> new IllegalArgumentException("Igreja not found with name: " + igrejaNome));

        IgrejaEbd igrejaEbd = new IgrejaEbd();
        igrejaEbd.setEbd(ebd);
        igrejaEbd.setIgreja(igreja);

        igrejaEbdRepository.save(igrejaEbd);

        // Adding to the sets to maintain consistency
        ebd.getIgrejaEbds().add(igrejaEbd);
        igreja.getIgrejaEbds().add(igrejaEbd);

        ebdRepository.save(ebd);
        igrejaRepository.save(igreja);
    }


    public Ebd obterEbdPorIgrejaId(Long igrejaId) {
        Optional<Ebd> ebdOptional = ebdRepository.findByIgrejaId(igrejaId);
        return ebdOptional.orElseThrow(() -> new EntityNotFoundException("Ebd não encontrada para o igrejaId: " + igrejaId));
    }

    /**relatorio de secretaria
     * Metodo para obter os dados de total de aulas por turma e ebd
     * Este método busca por data e ebd as turmas e aulas para somar
     * seus dados e exibir o resultado para estudo de resultados
    */

    public RelatorioDTO gerarRelatorio(Long ebdId, LocalDate data, Integer mes, Integer trimestre, Integer ano) {
        Ebd ebd = ebdRepository.findById(ebdId).orElseThrow(() -> new EntityNotFoundException("EBD não encontrada"));
        Set<Turma> turmas = ebd.getEbdTurmas().stream()
                .map(EbdTurma::getTurma)
                .collect(Collectors.toSet());

        List<Aula> aulas;
        if (data != null) {
            aulas = aulaRepository.findByTurmasEData(turmas, data);
        } else if (mes != null) {
            aulas = aulaRepository.findByTurmasEMesEAno(turmas, mes, ano);
        } else if (trimestre != null) {
            aulas = aulaRepository.findByTurmasETrimestreEAno(turmas, trimestre, ano);
        } else {
            throw new IllegalArgumentException("Data, mês ou trimestre devem ser fornecidos");
        }

        Integer totalMatriculados = 0;
        Integer totalPresentes = 0;
        Integer totalAusentes = 0;
        Integer totalVisitantes = 0;
        Integer totalAssistencia = 0;
        Integer totalBiblias = 0;
        Integer totalRevistas = 0;
        Double totalOferta = 0.0;

        Map<Turma, TurmasDTO> turmasDTOMap = new HashMap<>();

        // Itera pelas aulas para calcular os totais por turma e gerais
        for (Aula aula : aulas) {
            for (AulaTurma aulaTurma : aula.getAulaTurmas()) {
                Turma turma = aulaTurma.getTurma();

                // Se a turma ainda não foi processada, inicializa o DTO correspondente
                if (!turmasDTOMap.containsKey(turma)) {
                    TurmasDTO turmasDTO = new TurmasDTO();
                    turmasDTO.setNomeTurma(turma.getNome());
                    turmasDTOMap.put(turma, turmasDTO);
                }

                TurmasDTO turmasDTO = turmasDTOMap.get(turma);

                // Atualiza os totais da turma
                turmasDTO.setTotalMatriculados(
                        (turmasDTO.getTotalMatriculados() != null ? turmasDTO.getTotalMatriculados() : 0)
                                + Integer.parseInt(aula.getAlunosMatriculados())
                );

                turmasDTO.setTotalPresentes(
                        (turmasDTO.getTotalPresentes() != null ? turmasDTO.getTotalPresentes() : 0)
                                + Integer.parseInt(aula.getPresentes())
                );

                turmasDTO.setTotalAusentes(
                        (turmasDTO.getTotalAusentes() != null ? turmasDTO.getTotalAusentes() : 0)
                                + Integer.parseInt(aula.getAusentes())
                );

                turmasDTO.setTotalVisitantes(
                        (turmasDTO.getTotalVisitantes() != null ? turmasDTO.getTotalVisitantes() : 0)
                                + Integer.parseInt(aula.getVisitantes())
                );

                turmasDTO.setTotalAssistencia(
                        (turmasDTO.getTotalAssistencia() != null ? turmasDTO.getTotalAssistencia() : 0)
                                + Integer.parseInt(aula.getTotalAssistencia())
                );

                turmasDTO.setTotalBiblias(
                        (turmasDTO.getTotalBiblias() != null ? turmasDTO.getTotalBiblias() : 0)
                                + Integer.parseInt(aula.getBiblias())
                );

                turmasDTO.setTotalRevistas(
                        (turmasDTO.getTotalRevistas() != null ? turmasDTO.getTotalRevistas() : 0)
                                + Integer.parseInt(aula.getRevistas())
                );

                turmasDTO.setTotalOferta(
                        (turmasDTO.getTotalOferta() != null ? turmasDTO.getTotalOferta() : 0.0)
                                + (aula.getOferta() != null ? aula.getOferta() : 0.0)
                );



                // Atualiza os totais gerais
                totalMatriculados += Integer.parseInt(aula.getAlunosMatriculados());
                totalPresentes += Integer.parseInt(aula.getPresentes());
                totalAusentes += Integer.parseInt(aula.getAusentes());
                totalVisitantes += Integer.parseInt(aula.getVisitantes());
                totalAssistencia += Integer.parseInt(aula.getTotalAssistencia());
                totalBiblias += Integer.parseInt(aula.getBiblias());
                totalRevistas += Integer.parseInt(aula.getRevistas());
                totalOferta += aula.getOferta();
            }
        }

        // Prepara o relatório final
        RelatorioDTO relatorio = new RelatorioDTO();
        relatorio.setTotalMatriculados(totalMatriculados);
        relatorio.setTotalPresentes(totalPresentes);
        relatorio.setTotalAusentes(totalAusentes);
        relatorio.setTotalVisitantes(totalVisitantes);
        relatorio.setTotalAssistencia(totalAssistencia);
        relatorio.setTotalBiblias(totalBiblias);
        relatorio.setTotalRevistas(totalRevistas);
        relatorio.setTotalOferta(totalOferta);

        // Adiciona a lista de turmas ao relatório
        relatorio.setTurmas(new ArrayList<>(turmasDTOMap.values()));

        return relatorio;
    }


}
