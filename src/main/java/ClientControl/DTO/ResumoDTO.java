package ClientControl.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ResumoDTO {
    private long totalClientes;
    private BigDecimal totalPago;
    private BigDecimal totalPendente;
    private BigDecimal totalGeral;
    private BigDecimal totalVencido;
    private BigDecimal totalFaltaReceber;
}