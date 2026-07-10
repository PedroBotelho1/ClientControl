package ClientControl.service;

import ClientControl.DTO.ResumoDTO;
import ClientControl.model.Cliente;
import ClientControl.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
            throw new RuntimeException("Cliente não encontrado");
        }
        repository.deleteById(id);
    }

    public Cliente togglePago(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        cliente.setPago(!cliente.isPago());
        return repository.save(cliente);
    }

    public ResumoDTO resumo() {

        // Mostra o total de clientes
       List<Cliente> todos = repository.findAll();
       int totalClientes = todos.size();

       // Mostra o total pago e pendente
       double totalPago = 0;
       double totalPendente = 0;

       for(Cliente cliente : todos) {
           if(cliente.isPago()) {
               totalPago += cliente.getValor();
           } else {
               totalPendente += cliente.getValor();
           }
       }

       // Mostra o total para receber
       double totalGeral = totalPago + totalPendente;

       return new ResumoDTO(totalClientes, totalPago, totalPendente, totalGeral);
    }
}
