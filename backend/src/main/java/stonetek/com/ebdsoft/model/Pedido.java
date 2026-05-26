package stonetek.com.ebdsoft.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "pedido")
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trimestre;

    private String nome;

    @Column(name = "data_pedido")
    private LocalDate dataPedido;

    @Column(name = "data_entrega_prevista")
    private LocalDate dataEntregaPrevista;

    private String status;

    @ManyToOne
    @JoinColumn(name = "igreja_id")
    private Igreja igreja;

    @OneToMany(mappedBy = "pedido", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("pedido-pedidoRevista")
    private Set<PedidoRevista> pedidoRevistas = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @DecimalMin("0.0")
    private BigDecimal total;
}

