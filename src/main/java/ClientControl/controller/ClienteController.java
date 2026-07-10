package ClientControl.controller;

import ClientControl.DTO.ResumoDTO;
import ClientControl.model.Cliente;
import ClientControl.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/clientes")
@CrossOrigin(origins = "http://localhost:5173")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @GetMapping
    public ResponseEntity<List<Cliente>> findAll() {
        List<Cliente> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Cliente> insert(@RequestBody Cliente obj) {
        obj = service.salvar(obj);
        return ResponseEntity.status(201).body(obj);
    }

    @PatchMapping(value = "/{id}/toggle-pago")
    public ResponseEntity<Cliente> togglePago(@PathVariable Long id) {
        Cliente obj = service.togglePago(id);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/resumo")
    public ResponseEntity<ResumoDTO> resumo() {
        ResumoDTO dto = service.resumo();
        return ResponseEntity.ok().body(dto);
    }
}
