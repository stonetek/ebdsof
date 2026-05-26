package stonetek.com.ebdsoft.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "pagamento_parcela")
public class PagamentoParcela implements Serializable {

    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne//(fetch = FetchType.EAGER)
    @JoinColumn(name = "pagamento_id")
    @JsonBackReference("pagamento-pagamentoParcela")
    private Pagamento pagamento;

    @ManyToOne//(fetch = FetchType.EAGER)
    @JoinColumn(name = "parcela_id")
    @JsonBackReference("parcela-pagamentoParcela")
    private Parcela parcela;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PagamentoParcela that = (PagamentoParcela) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
