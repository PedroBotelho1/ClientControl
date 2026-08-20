package ClientControl.model;

import ClientControl.enums.StatusCliente;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do cliente não pode ser vazio.")
    private String nome;

    @NotBlank(message = "O nome da escola é obrigatório.")
    private String escola;

    @NotBlank(message = "O valor da mensalidade é obrigatório.")
    @Positive(message = "O valor da mensalidade deve ser maior que zero.")
    private Double valor;
    private boolean pago;
    private LocalDate vencimento;

    @Transient
    public StatusCliente getStatus() {
        if(this.pago) {
            return StatusCliente.PAGO;
        }
        if(this.vencimento != null && LocalDate.now().isAfter(this.vencimento)) {
            return StatusCliente.VENCIDO;
        }

        return StatusCliente.PENDENTE;
    }
}
