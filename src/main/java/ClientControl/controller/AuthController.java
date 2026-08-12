package ClientControl.controller;

import ClientControl.DTO.LoginRequest;
import ClientControl.DTO.LoginResponse;
import ClientControl.model.Cliente;
import ClientControl.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = service.login(request);

        return ResponseEntity.ok(response);
    }
}
