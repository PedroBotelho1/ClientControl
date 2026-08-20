package ClientControl.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(

        @NotBlank(message = "O email não pode ficar em branco.")
        @Email(message = "O formato do email é inválido.")
        String email,

        @NotBlank(message = "A senha não pode ficar em branco.")
        String senha
) {
}
