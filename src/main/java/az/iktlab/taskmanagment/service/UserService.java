package az.iktlab.taskmanagment.service;

import az.iktlab.taskmanagment.model.jwt.JwtToken;
import az.iktlab.taskmanagment.model.request.otp.UserRecoverAccountOTPRequest;
import az.iktlab.taskmanagment.model.request.otp.UserRecoverAccountRequest;
import az.iktlab.taskmanagment.model.request.UserSignInRequest;
import az.iktlab.taskmanagment.model.request.UserSignUpRequest;

import java.security.Principal;

public interface UserService {
    JwtToken signUp(UserSignUpRequest userSignUpDto);
    JwtToken signIn(UserSignInRequest userSignInDto);
    void sendOTPForRecover(UserRecoverAccountRequest userRecoverAccountRequest);

    void recoverAccount(UserRecoverAccountOTPRequest userRecoverAccountOTPRequest);
}
