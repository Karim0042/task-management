package az.iktlab.taskmanagment.model.request;

import az.iktlab.taskmanagment.validator.Password;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSignInRequest {
    @NotEmpty(message = "Email is required")
    private String email;
    @Password
    private String password;
    @NotNull
    private boolean rememberMe;
}
