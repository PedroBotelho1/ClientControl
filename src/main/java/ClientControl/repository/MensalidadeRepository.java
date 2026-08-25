package ClientControl.repository;

import ClientControl.enums.StatusCliente;
import ClientControl.model.Mensalidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MensalidadeRepository extends JpaRepository<Mensalidade, Long> {

    List<Mensalidade> findByClienteId(Long clienteId);

    // Traz faturas de um status específico em um mês e ANO específicos
    List<Mensalidade> findByStatusAndMesReferenciaAndAnoReferencia(StatusCliente status, Integer mes, Integer ano);

    // Verifica se a mensalidade já foi gerada para não cobrar duplicado
    boolean existsByClienteIdAndMesReferenciaAndAnoReferencia(Long clienteId, Integer mes, Integer ano);

    // Verifica rapidamente se o cliente tem faturas pendentes/vencidas
    boolean existsByClienteIdAndStatus(Long clienteId, StatusCliente status);

    // Busca todas as faturas que têm um status X e venceram antes de uma data Y
    List<Mensalidade> findByStatusAndDataVencimentoBefore(StatusCliente status, LocalDate data);
}
