package ClientControl.service;

import ClientControl.enums.StatusCliente;
import ClientControl.exception.RegraDeNegocioException;
import ClientControl.model.Cliente;
import ClientControl.model.Mensalidade;
import ClientControl.repository.ClienteRepository;
import ClientControl.repository.MensalidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class MensalidadeService {

    @Autowired
    private MensalidadeRepository mensalidadeRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Mensalidade> findAll() {
        return mensalidadeRepository.findAll();
    }

    public Mensalidade gerarMensalidade(Long clienteId, Integer mes, Integer ano) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RegraDeNegocioException("Cliente não encontrado."));

        // Evita gerar dois boletos para o mesmo aluno no mesmo mês
        boolean jaExiste = mensalidadeRepository
                .existsByClienteIdAndMesReferenciaAndAnoReferencia(clienteId, mes, ano);

        if (jaExiste) {
            throw new RegraDeNegocioException("Já existe uma mensalidade para este cliente neste mês e ano.");
        }

        Mensalidade nova = new Mensalidade();
        nova.setCliente(cliente);
        nova.setMesReferencia(mes);
        nova.setAnoReferencia(ano);
        nova.setValor(cliente.getValor());
        nova.setStatus(StatusCliente.PENDENTE);

        // Previne erro caso o cliente pague dia 31 e o mês atual só tenha 28 ou 30 dias
        try {
            nova.setDataVencimento(LocalDate.of(ano, mes, cliente.getDiaVencimento()));
        } catch (Exception e) {
            nova.setDataVencimento(LocalDate.of(ano, mes, 1).with(TemporalAdjusters.lastDayOfMonth()));
        }

        return mensalidadeRepository.save(nova);
    }

    public Mensalidade pagarMensalidade(Long id) {
        Mensalidade mensalidade = mensalidadeRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Mensalidade não encontrada."));

        if (mensalidade.getStatus() == StatusCliente.PAGO) {
            throw new RegraDeNegocioException("Esta mensalidade já consta como paga.");
        }

        mensalidade.setStatus(StatusCliente.PAGO);
        mensalidade.setDataPagamento(LocalDate.now());

        return mensalidadeRepository.save(mensalidade);
    }

    // Usaria apenas para casos específicos, caso nao vá cobrar naquele mês a mensalidade e precise apagar de última hora
    public void deletar(Long id) {
        if(!mensalidadeRepository.existsById(id)) {
            throw new RegraDeNegocioException("Mensalidade não encontrada");
        }
        mensalidadeRepository.deleteById(id);
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void gerarMensalidadesAutomaticas() {
        LocalDate hoje = LocalDate.now();
        Integer mesAtual = hoje.getMonthValue();
        Integer anoAtual = hoje.getYear();

        List<Cliente> clientes = clienteRepository.findAll();

        for (Cliente cliente : clientes) {
            // Para verificar se a mensalidade desse aluno já foi gerada neste mês
            boolean jaExiste = mensalidadeRepository
                    .existsByClienteIdAndMesReferenciaAndAnoReferencia(cliente.getId(), mesAtual, anoAtual);

            // Se não existir, o sistema cria sozinho
            if (!jaExiste) {
                Mensalidade nova = new Mensalidade();
                nova.setCliente(cliente);
                nova.setMesReferencia(mesAtual);
                nova.setAnoReferencia(anoAtual);
                nova.setValor(cliente.getValor());
                nova.setStatus(StatusCliente.PENDENTE);

                try {
                    nova.setDataVencimento(LocalDate.of(anoAtual, mesAtual, cliente.getDiaVencimento()));
                } catch (Exception e) {
                    nova.setDataVencimento(LocalDate.of(anoAtual, mesAtual, 1).with(TemporalAdjusters.lastDayOfMonth()));
                }

                mensalidadeRepository.save(nova);
                System.out.println("Mensalidade gerada automaticamente para: " + cliente.getNome());
            }
        }
    }
}