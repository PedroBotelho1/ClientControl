package ClientControl.service;

import ClientControl.DTO.ResumoDTO;
import ClientControl.enums.StatusCliente;
import ClientControl.exception.RegraDeNegocioException;
import ClientControl.model.Cliente;
import ClientControl.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public List<Cliente> findAll() {
        return repository.findAll();
    }

    public Cliente salvar(Cliente c) {
        return repository.save(c);
    }

    public void deletar(Long id) {
        if(!repository.existsById(id)) {
            // Troquei para a sua RegraDeNegocioException para manter o padrão
            throw new RegraDeNegocioException("Cliente não encontrado");
        }
        repository.deleteById(id);
    }

    public ResumoDTO resumo() {
        List<Cliente> todos = repository.findAll();
        int totalClientes = todos.size();

        // Iniciando os valores com BigDecimal.ZERO
        BigDecimal totalPago = BigDecimal.ZERO;
        BigDecimal totalPendente = BigDecimal.ZERO;
        BigDecimal totalVencido = BigDecimal.ZERO;

        for(Cliente cliente : todos) {
            StatusCliente status = cliente.getStatus();

            if(status == StatusCliente.PAGO) {
                totalPago = totalPago.add(cliente.getValor());
            } else if(status == StatusCliente.VENCIDO) {
                totalVencido = totalVencido.add(cliente.getValor());
            } else if(status == StatusCliente.PENDENTE) {
                totalPendente = totalPendente.add(cliente.getValor());
            }
        }

        // Somando os totais gerais
        BigDecimal totalGeral = totalPago.add(totalPendente).add(totalVencido);
        BigDecimal totalFaltaReceber = totalPendente.add(totalVencido);

        return new ResumoDTO(totalClientes, totalPago, totalPendente, totalGeral, totalVencido, totalFaltaReceber);
    }
}