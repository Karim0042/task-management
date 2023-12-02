package az.iktlab.taskmanagment.controller;


import az.iktlab.taskmanagment.model.request.otp.UserRecoverAccountOTPRequest;
import az.iktlab.taskmanagment.model.request.otp.UserRecoverAccountRequest;
import az.iktlab.taskmanagment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static az.iktlab.taskmanagment.util.ResponseBuilder.buildResponse;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/public")
public class PublicController {
    private final UserService userService;

    @PostMapping("/recover")
    public HttpStatus sendRecoverMail(@RequestBody @Valid UserRecoverAccountRequest userRecoverAccountRequest) {
        userService.sendOTPForRecover(userRecoverAccountRequest);
        return HttpStatus.OK;
    }

    @PostMapping("/recover/otp")
    public HttpStatus useOTPCode(@RequestBody @Valid UserRecoverAccountOTPRequest userRecoverAccountOTPRequest) {
        userService.recoverAccount(userRecoverAccountOTPRequest);
        return HttpStatus.OK;
    }
}
