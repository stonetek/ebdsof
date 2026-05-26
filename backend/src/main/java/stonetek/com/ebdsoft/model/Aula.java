package stonetek.com.ebdsoft.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "aula")
public class Aula implements Serializable {

    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    private String licao;

    private LocalDate dia;

    private String alunosMatriculados;
	
    private String trimestre;
	
    private String ausentes;
	
    private String presentes;
	
    private String visitantes;
	
    private String totalAssistencia;
	
    private String biblias;
	
    private String revistas;
	
    private Double oferta;

    @OneToMany(mappedBy = "aula", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference("aula-professorAulas")
    private Set<ProfessorAula> professorAulas = new HashSet<>();

    @OneToMany(mappedBy = "aula", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference("aula-alunoAula")
    private Set<AlunoAula> alunoAulas = new HashSet<>();

    @OneToMany(mappedBy = "aula", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference("aula-aulaTurmas")
    private Set<AulaTurma> aulaTurmas = new HashSet<>();


    /**
     * Método para buscar a soma das ofertas das aulas de uma turma em um mês e trimestre específicos.
     *
     * @param turma Turma para a qual deseja-se obter a soma das ofertas.
     * @param mes Mês (1 a 12) para filtrar as aulas.
     * @param trimestre Trimestre (1, 2, 3 ou 4) para filtrar as aulas.
     * @return Soma das ofertas das aulas da turma no mês e trimestre especificados.
     */
    public Double somaOfertasPorTurmaEMesETrimestre(Turma turma, int mes, int trimestre) {
        Double somaOfertas = 0.0;

        // Itera sobre as AulaTurma da turma especificada
        for (AulaTurma aulaTurma : turma.getAulaTurmas()) {
            Aula aula = aulaTurma.getAula();

            // Verifica se a aula pertence ao mês e trimestre desejados
            if (getMonth(aula.getDia()) == mes && getTrimestre(aula.getDia()) == trimestre) {
                somaOfertas += aula.getOferta();
            }
        }

        return somaOfertas;
    }

    /** Método auxiliar para obter o mês a partir de uma data */
    public  int getMonth(LocalDate date) {
        return date.getMonthValue();
    }

    /** Método auxiliar para obter o trimestre a partir de uma data */
    public Integer getTrimestre(LocalDate date) {
        int mes = date.getMonthValue(); // getMonthValue() retorna o mês como um int de 1 a 12
        return (mes - 1) / 3 + 1;
    }

    public Set<ProfessorAula> getProfessorAulas() {
        return professorAulas;
    }

    public void setProfessorAulas(Set<ProfessorAula> professorAulas) {
        this.professorAulas = professorAulas;
    }

    public Professor getProfessor() {
        if (professorAulas != null && !professorAulas.isEmpty()) {
            return professorAulas.iterator().next().getProfessor();  // Supondo que há pelo menos um ProfessorAula
        }
        return null;
    }
}
