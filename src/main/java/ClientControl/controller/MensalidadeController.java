package ClientControl.controller;


import ClientControl.model.Mensalidade;
import ClientControl.service.MensalidadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/mensalidades")
@CrossOrigin(origins = "http://localhost:5173")
public class MensalidadeController {

    @Autowired
    private MensalidadeService mensalidadeService;

    @GetMapping
    public ResponseEntity<List<Mensalidade>> findAll() {
        List<Mensalidade> list = mensalidadeService.findAll();

        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Mensalidade> gerarMensalidade(@RequestParam Long ClienteId, @RequestParam Integer mes, @RequestParam Integer ano) {

        Mensalidade obj = mensalidadeService.gerarMensalidade(ClienteId, mes, ano);
        return ResponseEntity.status(201).body(obj);
    }

    @PatchMapping(value = "/{id}/pagar")
    public ResponseEntity<Mensalidade> pagarMensalidade(@PathVariable Long id) {
        Mensalidade obj = mensalidadeService.pagarMensalidade(id);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Mensalidade> deletar(@PathVariable Long id) {
        mensalidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
