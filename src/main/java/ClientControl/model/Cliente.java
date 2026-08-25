package ClientControl.model;

import ClientControl.enums.StatusCliente;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

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

    // Trocamos @NotBlank por @NotNull porque BigDecimal não é String
    @NotNull(message = "O valor da mensalidade é obrigatório.")
    @Positive(message = "O valor da mensalidade deve ser maior que zero.")
    private BigDecimal valor;

    // Trocamos o LocalDate por um número inteiro (ex: 5, 10, 20)
    @NotNull(message = "O dia de vencimento é obrigatório.")
    private Integer diaVencimento;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente")
    private List<Mensalidade> mensalidades;

    @Transient
    public StatusCliente getStatus() {
        // Mudança aqui: Cliente novo entra como PENDENTE, pois precisa pagar o 1º mês para rodar
        if (this.mensalidades == null || this.mensalidades.isEmpty()) {
            return StatusCliente.PENDENTE;
        }

        boolean temPendente = false;

        for (Mensalidade mensalidade : this.mensalidades) {
            if (mensalidade.getStatus() == StatusCliente.VENCIDO) {
                return StatusCliente.VENCIDO;
            }
            if (mensalidade.getStatus() == StatusCliente.PENDENTE) {
                temPendente = true;
            }
        }

        if (temPendente) {
            return StatusCliente.PENDENTE;
        }

        return StatusCliente.PAGO;
    }
}