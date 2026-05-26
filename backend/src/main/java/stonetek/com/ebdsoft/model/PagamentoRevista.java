package stonetek.com.ebdsoft.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "pagamento_revista")
public class PagamentoRevista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "revista_id")
    @JsonBackReference("professor-pagamentoRevista")
    private Revista revista;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(nullable = true)
    private Boolean pago;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    private short parcela;

    @Column(name = "valor_pago", precision = 10, scale = 2)
    private Double valorPago;

    public Boolean getPago() {
        return pago;
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }

    public Boolean isPago() {
        return pago;
    }

}
