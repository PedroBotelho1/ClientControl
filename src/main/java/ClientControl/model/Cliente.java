package ClientControl.model;

import ClientControl.enums.StatusCliente;
import jakarta.persistence.*;
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

    private String nome;
    private String escola;
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
