package ClientControl.service;

import ClientControl.DTO.LoginRequest;
import ClientControl.DTO.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.email(), request.senha());

        Authentication auth = authenticationManager.authenticate(authToken);

        UserDetails user = (UserDetails) authToken.getPrincipal();

        String token = jwtService.gerarToken(user);

        return new LoginResponse(token);
    }
}
