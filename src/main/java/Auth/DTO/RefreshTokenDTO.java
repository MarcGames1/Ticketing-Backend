package Auth.DTO;

import jakarta.validation.constraints.NotEmpty;

public class RefreshTokenDTO {
    @NotEmpty
    public String accessToken;
    @NotEmpty
    public String refreshToken;
}
