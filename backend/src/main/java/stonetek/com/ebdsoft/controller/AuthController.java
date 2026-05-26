package stonetek.com.ebdsoft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import stonetek.com.ebdsoft.dto.dtosespecificos.CodigoDTO;
import stonetek.com.ebdsoft.dto.request.CodeValidationRequest;
import stonetek.com.ebdsoft.dto.request.EmailRequest;
import stonetek.com.ebdsoft.dto.request.JwtRequest;
import stonetek.com.ebdsoft.dto.request.UsuarioRequest;
import stonetek.com.ebdsoft.dto.response.JwtResponse;
import stonetek.com.ebdsoft.dto.response.UsuarioResponse;
import stonetek.com.ebdsoft.model.Usuario;
import stonetek.com.ebdsoft.service.*;
import stonetek.com.ebdsoft.util.JwtUtil;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class AuthController {


    private final AuthenticationManager authenticationManager;

    private final MyUserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    private final UsuarioService usuarioService;

    private final AlunoService alunoService;

    private final ProfessorService professorService;

    private final PerfilService perfilService;

    private final IgrejaService igrejaService;

    private final VerificationService verificationService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody JwtRequest authenticationRequest) throws Exception {
        try {
            System.out.println("Tentativa de autenticação para usuário: " + authenticationRequest.getUsername());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            System.out.println("Falha na autenticação: " + e.getMessage());
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        Usuario usuario = usuarioService.findByUsername(authenticationRequest.getUsername());
        String nomeUsuario = usuario.getNome();

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        Long usuarioId = usuario.getId();
        Long perfilId = perfilService.findPerfilIdByUsuarioId(usuarioId);
        String nomePerfil = perfilService.findPerfilNomeById(perfilId);

        List<String> authorities = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toList());

        System.out.println("Perfis normalizados do usuário: " + authorities);

        boolean isAluno = authorities.contains("ROLE_ALUNO");
        boolean isProfessor = authorities.contains("ROLE_PROFESSOR");
        boolean isAdmin = authorities.contains("ROLE_ADMIN");
        boolean isAdminIgreja = authorities.contains("ROLE_ADMIN_IGREJA");
        boolean isSecretaria = authorities.contains("ROLE_SECRETARIA");
        boolean isCoordenador = authorities.contains("ROLE_COORDENADOR");

        Long igrejaId = null;
        Long classeId = null;

        if (isAluno) {
            igrejaId = igrejaService.findIgrejaIdByUsuarioId(usuarioId);
            classeId = alunoService.findAlunoIdByUsuarioId(usuarioId);
        } else if (isProfessor) {
            igrejaId = igrejaService.findIgrejaIdByUsuarioId(usuarioId);
            classeId = professorService.findProfessorIdByUsuarioId(usuarioId);
        } else if (isAdmin) {
            igrejaId = null; // Admin global, vê todas as igrejas
            classeId = null;
        } else if (isAdminIgreja || isSecretaria || isCoordenador) {
            igrejaId = igrejaService.findIgrejaIdByUsuarioId(usuarioId); // Restrito à própria igreja
            classeId = null;
        } else {
            throw new RuntimeException("Usuário não possui um papel reconhecido (aluno, professor, administrador, etc).");
        }

        final String jwt = jwtUtil.generateToken(userDetails.getUsername(), authorities, igrejaId);

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                authorities.stream().findFirst().orElse(null),
                usuarioId,
                perfilId,
                nomePerfil,
                classeId,
                igrejaId,
                nomeUsuario
        ));
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UsuarioRequest userRequest) {
        UsuarioResponse userResponse = usuarioService.salvar(userRequest);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(@RequestBody EmailRequest request) {
        if (usuarioService.existsByUsername(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já cadastrado.");
        }
        verificationService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok("Código enviado para o email!");
    }

    @PostMapping("/validate-code")
    public ResponseEntity<?> validarCodigo(@RequestBody CodigoDTO dto) {
        boolean valido = verificationService.verifyCode(dto.getEmail(), dto.getCode());
        if (valido) {
            String token = jwtUtil.generateTemporaryToken(dto.getEmail());
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Código inválido");
    }

}
