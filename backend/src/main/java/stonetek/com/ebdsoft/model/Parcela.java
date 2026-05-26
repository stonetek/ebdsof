package stonetek.com.ebdsoft.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "parcela")
public class Parcela implements Serializable {

    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numero;

    private BigDecimal valor;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    private Integer atraso;

    private String status;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @OneToMany(mappedBy = "parcela", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference("parcela-pagamentoParcela")
    private Set<PagamentoParcela> pagamentoParcelas = new HashSet<>();
}
