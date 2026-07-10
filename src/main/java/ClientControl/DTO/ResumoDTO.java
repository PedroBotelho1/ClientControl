package ClientControl.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResumoDTO {

    private long totalClientes;
    private double totalPago;
    private double totalPendente;
    private double totalGeral;
    private double totalVencido;
}
