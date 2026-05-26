package stonetek.com.ebdsoft.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import stonetek.com.ebdsoft.dto.mapper.PerfilMapper;
import stonetek.com.ebdsoft.dto.request.PerfilRequest;
import stonetek.com.ebdsoft.dto.response.PerfilResponse;
import stonetek.com.ebdsoft.exception.PerfilNotFoundException;
import stonetek.com.ebdsoft.model.*;
import stonetek.com.ebdsoft.repository.*;
import stonetek.com.ebdsoft.util.Perfilable;
import stonetek.com.ebdsoft.util.ResourceUriUtil;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final PerfilRepository perfilRepository;

    private final UsuarioPerfilRepository usuarioPerfilRepository;

    private final ProfessorRepository professorRepository;

    private final AlunoRepository alunoRepository;

    public List<PerfilResponse> listar() {
        List<Perfil> perfis = perfilRepository.findPerfilByOrderByNomeAsc();
        return PerfilMapper.converter(perfis);
    }

    public PerfilResponse salvar(PerfilRequest request) {
        Perfil perfil = PerfilMapper.converter(request);
        perfil = perfilRepository.save(perfil);
        ResourceUriUtil.addUriInResponseHeader(perfil.getId()); // Adiciona no header da requisição o recurso que foi criado
        return PerfilMapper.converter(perfil);
    }

    public PerfilResponse buscarPorId(Long idPerfil) {
        Optional<Perfil> perfil = perfilRepository.findById(idPerfil);
        if (perfil.isEmpty()) {
            throw new PerfilNotFoundException(String.valueOf(idPerfil));
        }
        return PerfilMapper.converter(perfil.get());
    }

    public PerfilResponse editar(Long idPerfil, PerfilRequest request) {
        PerfilResponse perfilEncontrado = buscarPorId(idPerfil);
        Perfil perfil = PerfilMapper.converter(perfilEncontrado);
        PerfilMapper.copyToProperties(request, perfil);
        perfil = perfilRepository.save(perfil);
        return PerfilMapper.converter(perfil);
    }

    public void excluir(Long idPerfil) {
        try {
            perfilRepository.deleteById(idPerfil);
        } catch (EmptyResultDataAccessException ex) {
            throw new PerfilNotFoundException(String.valueOf(idPerfil));
        }
    }

    //personalizados


    public List<Perfil> findPerfilByNome(String nome) {
        return perfilRepository.findByNomeOrderByNomeAsc(nome);
    }

    public Long findPerfilIdByUsuarioId(Long usuarioId) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId); // ou recupere o usuário se necessário

        Optional<UsuarioPerfil> usuarioPerfilOptional = usuarioPerfilRepository.findByUsuario(usuario);
        if (usuarioPerfilOptional.isPresent()) {
            UsuarioPerfil usuarioPerfil = usuarioPerfilOptional.get();
            Perfil perfil = usuarioPerfil.getPerfil();
            return perfil.getId();
        }
        return null; // ou lance uma exceção se preferir
    }

    public void vincularPerfil(Perfilable entidade, Perfil perfil) {
        entidade.setPerfil(perfil);
        if (entidade instanceof Aluno) {
            Aluno aluno = (Aluno) entidade;
            alunoRepository.save(aluno);
        } else if (entidade instanceof Professor) {
            Professor professor = (Professor) entidade;
            professorRepository.save(professor);
        }
    }

    public String findPerfilNomeById(Long perfilId) {
        return perfilRepository.findNomeById(perfilId);
    }
}
