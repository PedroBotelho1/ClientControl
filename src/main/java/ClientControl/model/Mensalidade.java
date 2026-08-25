package ClientControl.model;

import ClientControl.enums.StatusCliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mensalidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cliente cliente;

    private Integer mesReferencia;
    private Integer anoReferencia;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private StatusCliente status;
    private BigDecimal valor;
}
