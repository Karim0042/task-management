package az.iktlab.taskmanagment.model.request.otp;


import az.iktlab.taskmanagment.validator.Email;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UserRecoverAccountOTPRequest {
    @NotEmpty(message = "Otp is required")
    private String otp;
    @Email
    private String email;
    @NotEmpty(message = "New password is required")
    @Size(max = 20, message = "Passwor cannot be longer than 20 characters")
    private String newPassword;
}
