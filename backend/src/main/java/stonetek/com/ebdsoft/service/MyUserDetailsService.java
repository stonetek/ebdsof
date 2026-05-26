package stonetek.com.ebdsoft.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import stonetek.com.ebdsoft.dto.request.UsuarioRequest;
import stonetek.com.ebdsoft.model.Usuario;
import stonetek.com.ebdsoft.model.UsuarioPerfil;
import stonetek.com.ebdsoft.repository.UsuarioRepository;
import stonetek.com.ebdsoft.util.JwtUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        System.out.println("Usuário encontrado: " + user.getUsername());
        System.out.println("Senha codificada: " + user.getPassword());

        for (UsuarioPerfil up : user.getUsuarioPerfis()) {
            System.out.println("Perfil: " + up.getPerfil().getNome());
        }
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        authorities.forEach(auth -> System.out.println("Granted Authority: " + auth.getAuthority()));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }


   /* public void saveUser(Usuario user) {
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        System.out.println("Raw password: " + rawPassword);
        System.out.println("Encoded password: " + encodedPassword);
        usuarioRepository.save(user);
    } */

}


