package stonetek.com.ebdsoft.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails, Serializable {

    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true)
    private String username;

    private String nome;

    private String password;

    private Boolean status;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("usuario-usuarioPerfil")
    Set<UsuarioPerfil> usuarioPerfis = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "igreja_id")
    private Igreja igreja;

    public void setUsuarioPerfis(Set<UsuarioPerfil> usuarioPerfis) {
        this.usuarioPerfis = usuarioPerfis;
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return usuarioPerfis.stream()
                .map(usuarioPerfil -> new SimpleGrantedAuthority("ROLE_" + usuarioPerfil.getPerfil().getNome().toUpperCase()))
                .collect(Collectors.toSet());
    }


    public Boolean getStatus() {
        return status;
    }

    @Override
    public String getUsername() {
        return username;
    }

    //@Override
    public String getNome() {
        return nome;
    }

    public void setUsername(String username) throws IllegalArgumentException {
        if (isValidEmail(username)) {
            this.username = username;
        } else {
            throw new IllegalArgumentException("O username deve ser um e-mail válido.");
        }
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void main(String[] args) {
        try {
            Usuario user = new Usuario();
            user.setUsername("exemplo@dominio.com");
            System.out.println("Username válido: " + user.getUsername());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
