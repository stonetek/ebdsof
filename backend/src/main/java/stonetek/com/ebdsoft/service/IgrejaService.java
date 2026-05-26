package stonetek.com.ebdsoft.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import stonetek.com.ebdsoft.dto.dtosespecificos.*;
import stonetek.com.ebdsoft.dto.mapper.IgrejaMapper;
import stonetek.com.ebdsoft.dto.request.IgrejaRequest;
import stonetek.com.ebdsoft.dto.response.EbdTurmaResponse;
import stonetek.com.ebdsoft.dto.response.IgrejaEbdResponse;
import stonetek.com.ebdsoft.dto.response.IgrejaPublicaResponse;
import stonetek.com.ebdsoft.dto.response.IgrejaResponse;
import stonetek.com.ebdsoft.exception.IgrejaAlreadyEnrolledException;
import stonetek.com.ebdsoft.exception.IgrejaNotFoundException;
import stonetek.com.ebdsoft.exception.TurmaAlreadyEnrolledException;
import stonetek.com.ebdsoft.model.*;
import stonetek.com.ebdsoft.repository.*;
import stonetek.com.ebdsoft.util.ResourceUriUtil;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class IgrejaService {

    private final IgrejaRepository igrejaRepository;

    private final IgrejaEbdRepository igrejaEbdRepository;

    private final EbdRepository ebdRepository;

    private final AlunoTurmaRepository alunoTurmaRepository;

    private final UsuarioRepository usuarioRepository;

    private final AlunoRepository alunoRepository;

    private final TurmaRepository turmaRepository;

    private final EbdTurmaRepository ebdTurmaRepository;

    private final ProfessorRepository professorRepository;

    private final ProfessorTurmaRepository professorTurmaRepository;
    
    public List<IgrejaResponse> listar() {
        List<Igreja> igrejas = igrejaRepository.findAllByOrderByNomeAsc();
        return IgrejaMapper.converter(igrejas);
    }

    public List<IgrejaPublicaResponse> listarPublica() {
        List<Igreja> igrejas = igrejaRepository.findAllByOrderByNomeAsc();
        return IgrejaMapper.converterPublico(igrejas);
    }

    public IgrejaResponse salvar(IgrejaRequest request) {
        Igreja igreja = IgrejaMapper.converter(request);
        igreja = igrejaRepository.save(igreja);
        ResourceUriUtil.addUriInResponseHeader(igreja.getId()); // Adiciona no header da resquição o recurso que foi
                                                                   // criado
        return IgrejaMapper.converter(igreja);
    }

    public IgrejaResponse buscarPorId(Long idIgreja) {
        Optional<Igreja> igreja = igrejaRepository.findById(idIgreja);
        if (igreja.isEmpty()) {
            throw new IgrejaNotFoundException(idIgreja);
        }
        return IgrejaMapper.converter(igreja.get());
    }

    public IgrejaResponse editar(Long idIgreja, IgrejaRequest request) {
        IgrejaResponse igrejaEncontrada = buscarPorId(idIgreja);
        Igreja igreja = IgrejaMapper.converter(igrejaEncontrada);
        IgrejaMapper.copyToProperties(request, igreja);
        igreja = igrejaRepository.save(igreja);
        return IgrejaMapper.converter(igreja);
    }

    public void excluir(Long idIgreja) {
        try {
            igrejaRepository.deleteById(idIgreja);
        } catch (EmptyResultDataAccessException ex) {
            throw new IgrejaNotFoundException(idIgreja);
        }
    }

    //personalizados

    public IgrejaEbd criarVinculoIgrejaEbd(Long idIgreja, Long idEbd) {
        Igreja igreja = igrejaRepository.findById(idIgreja)
                .orElseThrow(() -> new EntityNotFoundException("Igreja not found"));
        Ebd ebd = ebdRepository.findById(idEbd)
                .orElseThrow(() -> new EntityNotFoundException("Ebd not found"));


        boolean exists = igrejaEbdRepository.existsByIgrejaAndEbd(igreja, ebd);
        if (exists) {
            throw new IgrejaAlreadyEnrolledException("A Igreja com id: " + idIgreja + " já está vinculada com a EBD de id: " + idEbd);
        }

        IgrejaEbd igrejaEbd = new IgrejaEbd();
        igrejaEbd.setIgreja(igreja);
        igrejaEbd.setEbd(ebd);


        return igrejaEbdRepository.save(igrejaEbd);
    }


    public IgrejaEbd atualizarVinculoIgrejaEbd(Long id, Long idIgreja, Long idEbd) {
        IgrejaEbd igrejaEbd = igrejaEbdRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("IgrejaEbd not found"));

        Igreja igreja = igrejaRepository.findById(idIgreja)
                .orElseThrow(() -> new EntityNotFoundException("Igreja not found"));

        Ebd ebd = ebdRepository.findById(idEbd)
                .orElseThrow(() -> new EntityNotFoundException("Ebd not found"));

        igrejaEbd.setIgreja(igreja);
        igrejaEbd.setEbd(ebd);

        return igrejaEbdRepository.save(igrejaEbd);
    }

    public IgrejaEbdResponse findIgrejaEbdById(Long id) {
        Optional<IgrejaEbd> igrejaEbdOptional = igrejaEbdRepository.findById(id);
        if (igrejaEbdOptional.isPresent()) {
            IgrejaEbd igrejaEbd = igrejaEbdOptional.get();
            return mapToResponse(igrejaEbd);
        } else {
            throw new ResourceNotFoundException("IgrejaEbd not found with id: " + id);
        }
    }

    private IgrejaEbdResponse mapToResponse(IgrejaEbd igrejaEbd) {
        IgrejaEbdResponse response = new IgrejaEbdResponse();
        response.setId(igrejaEbd.getId());
        response.setIdIgreja(igrejaEbd.getIgreja().getId());
        response.setIdEbd(igrejaEbd.getEbd().getId());
        response.setNomeEbd(igrejaEbd.getEbd().getNome());
        response.setNomeIgreja(igrejaEbd.getIgreja().getNome());
        return response;
    }

    public List<IgrejaEbdDTO> listarIgrejasEEbds() {
        List<Igreja> igrejas = igrejaRepository.findAll();

        // Mapear para DTOs que contenham id, nome da Turma e nome da Ebd
        return igrejas.stream()
                .flatMap(igreja -> igreja.getIgrejaEbds().stream()
                        .map(igrejaEbd -> new IgrejaEbdDTO(
                                igrejaEbd.getId(),
                                igrejaEbd.getEbd().getId(),
                                igreja.getId(),
                                igreja.getNome(),
                                igrejaEbd.getEbd().getNome())))
                .collect(Collectors.toList());
    }


    public List<IgrejasEbdsDTO> igrejasEEbds(Long igrejaId) {
        Igreja igreja = igrejaRepository.findById(igrejaId)
                .orElseThrow(() -> new ResourceNotFoundException("Igreja not found"));
        return igreja.getIgrejaEbds().stream()
                .map(igrejaEbd -> new IgrejasEbdsDTO(
                        igrejaEbd.getId(),
                        igreja.getId(), // Passa o ID da Igreja
                        igrejaEbd.getEbd().getId(), // Passa o ID da EBD
                        igreja.getNome(),
                        igrejaEbd.getEbd().getNome()))
                .collect(Collectors.toList());
    }


    // igreja com ebd e turmas

    public IgrejaEbdTurmasDTO getEbdAndTurmasByIgreja(Long idIgreja) {
        Igreja igreja = igrejaRepository.findById(idIgreja)
                .orElseThrow(() -> new EntityNotFoundException("Igreja not found"));

        IgrejaEbdTurmasDTO dto = new IgrejaEbdTurmasDTO();

        igreja.getIgrejaEbds().forEach(igrejaEbd -> {
            Ebd ebd = igrejaEbd.getEbd();
            dto.setIdEbd(ebd.getId());
            dto.setEbdNome(ebd.getNome());

            List<EbdTurmaDTO> turmas = ebd.getEbdTurmas().stream().map(ebdTurma -> {
                EbdTurmaDTO turmaDTO = new EbdTurmaDTO();
                turmaDTO.setId(ebdTurma.getTurma().getId());
                turmaDTO.setNomeTurma(ebdTurma.getTurma().getNome());
                // Set other fields as necessary
                return turmaDTO;
            }).collect(Collectors.toList());

            dto.setTurmas(turmas);
        });

        return dto;
    }

    public List<IgrejaEbdTurmasDTO> getEbdAndTurmasByIgrejas(Long idIgreja) {
        Igreja igreja = igrejaRepository.findById(idIgreja)
                .orElseThrow(() -> new EntityNotFoundException("Igreja not found"));

        List<IgrejaEbdTurmasDTO> result = new ArrayList<>();

        igreja.getIgrejaEbds().forEach(igrejaEbd -> {
            Ebd ebd = igrejaEbd.getEbd();

            IgrejaEbdTurmasDTO dto = new IgrejaEbdTurmasDTO();
            dto.setIdEbd(ebd.getId());
            dto.setEbdNome(ebd.getNome());

            List<EbdTurmaDTO> turmas = ebd.getEbdTurmas().stream().map(ebdTurma -> {
                EbdTurmaDTO turmaDTO = new EbdTurmaDTO();
                turmaDTO.setId(ebdTurma.getTurma().getId());
                turmaDTO.setNomeTurma(ebdTurma.getTurma().getNome());
                // Set other fields as necessary
                return turmaDTO;
            }).collect(Collectors.toList());

            dto.setTurmas(turmas);
            result.add(dto);
        });

        return result;
    }

//usuario igreja


    /**
     * Encontra o ID da Igreja associada a um usuário.
     *
     * @param usuarioId ID do usuário.
     * @return ID da Igreja.
     * @throws RuntimeException se a Igreja não for encontrada.
     */
    public Long findIgrejaIdByUsuarioId(Long usuarioId) {
        // Obter o usuário
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado com ID: " + usuarioId);
        }
        Usuario usuario = usuarioOpt.get();

        if (usuario.getIgreja() != null) {
            return usuario.getIgreja().getId();
        }

        // Verificar se o usuário é Aluno
        Optional<Aluno> alunoOpt = alunoRepository.findByUsuarioId(usuarioId);
        if (alunoOpt.isPresent()) {
            Aluno aluno = alunoOpt.get();
            return getIgrejaIdFromAluno(aluno);
        }

        // Verificar se o usuário é Professor
        Optional<Professor> professorOpt = professorRepository.findByUsuarioId(usuarioId);
        if (professorOpt.isPresent()) {
            Professor professor = professorOpt.get();
            return getIgrejaIdFromProfessor(professor);
        }

        List<String> authorities = usuario.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        boolean isAdmin = authorities.contains("administrador");
        boolean isAdminIgreja = authorities.contains("admin_igreja");

        if (isAdmin || isAdminIgreja) {
            return null; // Administradores e admin_igreja não precisam estar associados a uma igreja
        }


        // Se o usuário não for Aluno nem Professor, lançar exceção
        throw new RuntimeException("Usuário não está associado a um Aluno ou Professor.");
    }

    /**
     * Obtém o ID da Igreja a partir de um Aluno.
     *
     * @param aluno Aluno.
     * @return ID da Igreja.
     * @throws RuntimeException se a Igreja não for encontrada.
     */
    private Long getIgrejaIdFromAluno(Aluno aluno) {
        // Obter as Turmas do Aluno
        List<AlunoTurma> alunoTurmas = alunoTurmaRepository.findByAlunoId(aluno.getId());
        if (alunoTurmas.isEmpty()) {
            throw new RuntimeException("Aluno não está associado a nenhuma Turma.");
        }

        // Para cada Turma, navegar para Igreja via EbdTurma e IgrejaEbd
        for (AlunoTurma alunoTurma : alunoTurmas) {
            Optional<Turma> turmaOpt = turmaRepository.findById(alunoTurma.getTurma().getId());
            if (turmaOpt.isEmpty()) {
                continue;
            }
            Turma turma = turmaOpt.get();

            Optional<EbdTurma> ebdTurmaOpt = ebdTurmaRepository.findOptionalByTurmaId(turma.getId());
            if (ebdTurmaOpt.isEmpty()) {
                continue;
            }
            EbdTurma ebdTurma = ebdTurmaOpt.get();

            Optional<IgrejaEbd> igrejaEbdOpt = igrejaEbdRepository.findByEbdId(ebdTurma.getEbd().getId());
            if (igrejaEbdOpt.isEmpty()) {
                continue;
            }
            IgrejaEbd igrejaEbd = igrejaEbdOpt.get();

            return igrejaEbd.getIgreja().getId();
        }

        throw new RuntimeException("Igreja não encontrada para o Aluno.");
    }

    /**
     * Obtém o ID da Igreja a partir de um Professor.
     *
     * @param professor Professor.
     * @return ID da Igreja.
     * @throws RuntimeException se a Igreja não for encontrada.
     */
    private Long getIgrejaIdFromProfessor(Professor professor) {
        // Obter as Turmas do Professor
        List<ProfessorTurma> professorTurmas = professorTurmaRepository.findByProfessorId(professor.getId());
        if (professorTurmas.isEmpty()) {
            throw new RuntimeException("Professor não está associado a nenhuma Turma.");
        }

        // Para cada Turma, navegar para Igreja via EbdTurma e IgrejaEbd
        for (ProfessorTurma professorTurma : professorTurmas) {
            Optional<Turma> turmaOpt = turmaRepository.findById(professorTurma.getTurma().getId());
            if (turmaOpt.isEmpty()) {
                continue;
            }
            Turma turma = turmaOpt.get();

            Optional<EbdTurma> ebdTurmaOpt = ebdTurmaRepository.findOptionalByTurmaId(turma.getId());
            if (ebdTurmaOpt.isEmpty()) {
                continue;
            }
            EbdTurma ebdTurma = ebdTurmaOpt.get();

            Optional<IgrejaEbd> igrejaEbdOpt = igrejaEbdRepository.findByEbdId(ebdTurma.getEbd().getId());
            if (igrejaEbdOpt.isEmpty()) {
                continue;
            }
            IgrejaEbd igrejaEbd = igrejaEbdOpt.get();

            return igrejaEbd.getIgreja().getId();
        }

        throw new RuntimeException("Igreja não encontrada para o Professor.");
    }

}
