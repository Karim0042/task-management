package az.iktlab.taskmanagment.model.request;

import az.iktlab.taskmanagment.validator.Email;
import az.iktlab.taskmanagment.validator.Password;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSignUpRequest {
    @NotEmpty(message = "Username is required")
    @Size(max = 40, message = "Username cannot be longer than 40 characters")
    private String username;
    @Email
    private String email;
    @Password
    private String password;
    @NotNull(message = "Born date is required")
    private Long bornDate;
    @NotNull
    private boolean rememberMe;
}
