package stonetek.com.ebdsoft.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "ebd_turma")
public class EbdTurma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_ebd")
    @JsonBackReference("ebd-ebdTurma")
    private Ebd ebd;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_turma")
    @JsonBackReference("turma-ebdTurma")
    private Turma turma;
    
}
