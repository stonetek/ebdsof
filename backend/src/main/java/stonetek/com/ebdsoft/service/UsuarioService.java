package stonetek.com.ebdsoft.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stonetek.com.ebdsoft.dto.mapper.UsuarioMapper;
import stonetek.com.ebdsoft.dto.request.UsuarioRequest;
import stonetek.com.ebdsoft.dto.response.UsuarioResponse;
import stonetek.com.ebdsoft.exception.UsuarioNotFoundException;
import stonetek.com.ebdsoft.model.*;
import stonetek.com.ebdsoft.repository.*;
import stonetek.com.ebdsoft.util.ResourceUriUtil;
import stonetek.com.ebdsoft.util.Usuarioable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final PerfilRepository perfilRepository;

    private final IgrejaRepository igrejaRepository;

    private final ProfessorRepository professorRepository;

    private final AlunoRepository alunoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public List<UsuarioResponse> listar() {
        List<Usuario> usuarios = usuarioRepository.findUsuarioByOrderByUsernameAsc();
        return UsuarioMapper.converter(usuarios);
    }

    public UsuarioResponse salvar(UsuarioRequest request) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByUsername(request.getUsername());
        if (usuarioExistente.isPresent()) {
            throw new RuntimeException("Username já existe.");
        }

        Usuario usuario = UsuarioMapper.converter(request, perfilRepository);
        String rawPassword = usuario.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        usuario.setPassword(encodedPassword);
        //System.out.println("Senha codificada: " + encodedPassword); // Log da senha codificada

        Set<UsuarioPerfil> usuarioPerfis = new HashSet<>();
        for (String perfilNome : request.getPerfis()) {
            Perfil perfil = perfilRepository.findByNome(perfilNome)
                    .orElseThrow(() -> new RuntimeException("Perfil não encontrado: " + perfilNome));
            //System.out.println("Perfil encontrado: " + perfilNome); // Log do perfil encontrado
            UsuarioPerfil usuarioPerfil = new UsuarioPerfil();
            usuarioPerfil.setUsuario(usuario);
            usuarioPerfil.setPerfil(perfil);
            usuarioPerfis.add(usuarioPerfil);
        }
        usuario.setUsuarioPerfis(usuarioPerfis);

        if (request.getIgrejaId() != null) {
            Igreja igreja = igrejaRepository.findById(request.getIgrejaId())
                    .orElseThrow(() -> new RuntimeException("Igreja não encontrada: " + request.getIgrejaId()));
            usuario.setIgreja(igreja);
        }


        usuario = usuarioRepository.save(usuario);
        ResourceUriUtil.addUriInResponseHeader(usuario.getId()); // Adiciona no header da requisição o recurso que foi criado
        return UsuarioMapper.converter(usuario);
    }



    public UsuarioResponse buscarPorId(Long idUsuario) {
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        if (usuario.isEmpty()) {
            throw new UsuarioNotFoundException(String.valueOf(idUsuario));
        }
        return UsuarioMapper.converter(usuario.get());
    }

    public UsuarioResponse editar(Long idUsuario, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNotFoundException(String.valueOf(idUsuario)));
        UsuarioMapper.copyToProperties(request, usuario);

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            // Log da senha recebida
            System.out.println("Senha recebida (editar): " + request.getPassword());

            String encodedPassword = passwordEncoder.encode(request.getPassword());
            usuario.setPassword(encodedPassword);

            // Log da senha codificada
            System.out.println("Senha codificada (editar): " + encodedPassword);
        }

        if (request.getIgrejaId() != null) {
            Igreja igreja = igrejaRepository.findById(request.getIgrejaId())
                    .orElseThrow(() -> new RuntimeException("Igreja não encontrada: " + request.getIgrejaId()));
            usuario.setIgreja(igreja);
        }

        for (String perfilNome : request.getPerfis()) {
            Perfil perfil = perfilRepository.findByNome(perfilNome)
                    .orElseThrow(() -> new RuntimeException("Perfil não encontrado: " + perfilNome));
            UsuarioPerfil usuarioPerfilExistente = usuario.getUsuarioPerfis().stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Perfil não encontrado para o usuário"));
            usuarioPerfilExistente.setPerfil(perfil);
        }

        usuario = usuarioRepository.save(usuario);
        System.out.println("Usuário atualizado: " + usuario.getUsername()); // Log do usuário atualizado

        return UsuarioMapper.converter(usuario);
    }

    public void excluir(Long idUsuario) {
        try {
            usuarioRepository.deleteById(idUsuario);
        } catch (EmptyResultDataAccessException ex) {
            throw new UsuarioNotFoundException(String.valueOf(idUsuario));
        }
    }

    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o nome de usuário: " + username));
    }

    public boolean existsByUsername(String username) {
        return usuarioRepository.findByUsername(username).isPresent();
    }

    public List<Usuario> findUsuariosByNome(String nome) {
        return usuarioRepository.findByNome(nome);
    }

    public void vincularUsuario(Aluno aluno, Usuario usuario) {
        aluno.setUsuario(usuario);
        alunoRepository.save(aluno);
    }

    public void vincularUsuario(Professor professor, Usuario usuario) {
        professor.setUsuario(usuario);
        professorRepository.save(professor);
    }

    public List<Usuario> buscarUsuariosPorIgreja(Long igrejaId) {
        return usuarioRepository.findByIgrejaId(igrejaId);
    }



}
