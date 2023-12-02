package az.iktlab.taskmanagment.controller;

import az.iktlab.taskmanagment.model.jwt.JwtToken;
import az.iktlab.taskmanagment.model.request.UserSignInRequest;
import az.iktlab.taskmanagment.model.request.UserSignUpRequest;
import az.iktlab.taskmanagment.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static az.iktlab.taskmanagment.util.ResponseBuilder.buildResponse;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "This endpoint help us to register",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The request was successful"),
                    @ApiResponse(responseCode = "400",
                            description = "There is incoming request validation error"),
                    @ApiResponse(responseCode = "409",
                            description = "There is a conflict with the current state of the resource, " +
                                    "preventing the request from being completed."),
                    @ApiResponse(responseCode = "417",
                            description = "The server cannot meet the expectations specified in the request"),
                    @ApiResponse(responseCode = "500", description = "An unexpected error occurred on the server.")
            })
    public JwtToken signUp(@RequestBody @Valid UserSignUpRequest userSignUpDto) {
        return userService.signUp(userSignUpDto);
    }

    @PostMapping("/signin")
    @Operation(summary = "This endpoint help us to sign in",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The request was successful"),
                    @ApiResponse(responseCode = "400",
                            description = "There is incoming request validation error"),
                    @ApiResponse(responseCode = "409",
                            description = "There is a conflict with the current state of the resource, " +
                                    "preventing the request from being completed."),
                    @ApiResponse(responseCode = "417",
                            description = "The server cannot meet the expectations specified in the request"),
                    @ApiResponse(responseCode = "500", description = "An unexpected error occurred on the server.")
            })
    public JwtToken signIn(@RequestBody @Valid UserSignInRequest userSignUpDto) {
        return userService.signIn(userSignUpDto);
    }
}
