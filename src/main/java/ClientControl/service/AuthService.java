package ClientControl.service;

import ClientControl.DTO.LoginRequest;
import ClientControl.DTO.LoginResponse;
import ClientControl.DTO.RegisterRequest;
import ClientControl.model.Usuario;
import ClientControl.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        //  Tenta achar o e-mail no banco. Se não achar, lança a sua exceção.
        usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Se passou da linha de cima, o usuário existe. Segue com a autenticação normal.
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.email(), request.senha());

        Authentication auth = authenticationManager.authenticate(authToken);

        UserDetails user = (UserDetails) authToken.getPrincipal();

        String token = jwtService.gerarToken(user);

        return new LoginResponse(token);
    }

    public Usuario registrar(RegisterRequest registerRequest) {
        // verifica se o email já existe no banco
        if(usuarioRepository.findByEmail(registerRequest.email()).isPresent()) {
            throw new RuntimeException("Este email já está cadastrado no sistema!");
        }

       String senhaCriptografada = passwordEncoder.encode(registerRequest.senha());

        Usuario novoUsuario = new Usuario(null, registerRequest.email(), senhaCriptografada);

        return usuarioRepository.save(novoUsuario);
    }
}
