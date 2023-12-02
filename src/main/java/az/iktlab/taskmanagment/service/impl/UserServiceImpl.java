package az.iktlab.taskmanagment.service.impl;

import az.iktlab.taskmanagment.config.CustomEventPublisher;
import az.iktlab.taskmanagment.entity.OTPSession;
import az.iktlab.taskmanagment.entity.Role;
import az.iktlab.taskmanagment.entity.User;
import az.iktlab.taskmanagment.error.exception.AuthenticationException;
import az.iktlab.taskmanagment.error.exception.OTPSessionExpiredException;
import az.iktlab.taskmanagment.error.exception.ResourceAlreadyExistsException;
import az.iktlab.taskmanagment.error.exception.ResourceNotFoundException;
import az.iktlab.taskmanagment.event.NewUserRegistrationEvent;
import az.iktlab.taskmanagment.event.OTPEvent;
import az.iktlab.taskmanagment.model.jwt.JwtToken;
import az.iktlab.taskmanagment.model.request.otp.UserRecoverAccountOTPRequest;
import az.iktlab.taskmanagment.model.request.otp.UserRecoverAccountRequest;
import az.iktlab.taskmanagment.model.request.UserSignInRequest;
import az.iktlab.taskmanagment.model.request.UserSignUpRequest;
import az.iktlab.taskmanagment.repository.OTPSessionRepository;
import az.iktlab.taskmanagment.repository.RoleRepository;
import az.iktlab.taskmanagment.repository.UserRepository;
import az.iktlab.taskmanagment.security.JWTProvider;
import az.iktlab.taskmanagment.service.UserService;
import az.iktlab.taskmanagment.util.DateHelper;
import az.iktlab.taskmanagment.validator.OTPGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;
    private final CustomEventPublisher eventPublisher;
    private final OTPSessionRepository sessionRepository;

    @Override
    public JwtToken signUp(UserSignUpRequest userSignUpDto) {
        boolean existByEmail = userRepository.existsByEmailAndIsDeletedFalse(userSignUpDto.getEmail());
        if (existByEmail) {
            throw new ResourceAlreadyExistsException("User already exists with this email: " + userSignUpDto.getEmail());
        }
        User user = buildNewUser(userSignUpDto);
        userRepository.save(user);
        NewUserRegistrationEvent event = NewUserRegistrationEvent.builder()
                .email(user.getEmail())
                .build();
        eventPublisher.publishEvent(event);
        return jwtProvider.getJWTToken(user.getId(),userSignUpDto.isRememberMe());
    }

    @Override
    public JwtToken signIn(UserSignInRequest userSignInDto) {
        User user = userRepository.findByEmailAndIsDeletedFalse(userSignInDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found with this email:"
                + userSignInDto.getEmail()));
        if (passwordEncoder.matches(userSignInDto.getPassword(), user.getPassword())) {
            return jwtProvider.getJWTToken(user.getId(),userSignInDto.isRememberMe());
        }
        throw new AuthenticationException("Bad credentials");
    }



    @Override
    public void sendOTPForRecover(UserRecoverAccountRequest userRecoverAccountRequest) {
        User user = userRepository.findByEmailAndIsDeletedFalse(userRecoverAccountRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with this email: "
                        + userRecoverAccountRequest.getEmail()));

        OTPSession otpSession = createOTPSession(user);
        sessionRepository.save(otpSession);
        OTPEvent otpEvent = OTPEvent.builder()
                .otp(otpSession.getOtpCode()).email(user.getEmail())
                .build();
        eventPublisher.publishEvent(otpEvent);
    }

    @Override
    public void recoverAccount(UserRecoverAccountOTPRequest userRecoverAccountOTPRequest) {
        User user = userRepository.findByEmailAndIsDeletedFalse(userRecoverAccountOTPRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        OTPSession otpSession = sessionRepository.findByOtpCodeAndUserIdAndIsUsedFalse(
                        userRecoverAccountOTPRequest.getOtp(), user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Otp session not found."));

        checkOtpSessionIsExpired(otpSession.getCreateDate());

        user.setPassword(passwordEncoder.encode(userRecoverAccountOTPRequest.getNewPassword()));
        userRepository.save(user);

    }

    private User buildNewUser(UserSignUpRequest userSignUpRequest) {
        Role role = roleRepository.findRoleByName("USER");
        return User.builder()
                .roles(Set.of(role))
                .email(userSignUpRequest.getEmail())
                .username(userSignUpRequest.getUsername())
                .password(passwordEncoder.encode(userSignUpRequest.getPassword()))
                .isDeleted(false)
                .build();
    }
    private static OTPSession createOTPSession(User user) {
        return OTPSession.builder()
                .userId(user.getId())
                .isUsed(false)
                .createDate(DateHelper.now())
                .otpCode(OTPGenerator.generate())
                .build();
    }
    private void checkOtpSessionIsExpired(Date createDate) {
        Calendar now = Calendar.getInstance();
        Calendar otpCreateDate = Calendar.getInstance();
        otpCreateDate.setTime(createDate);
        otpCreateDate.add(Calendar.MINUTE, 3);
        if (now.after(otpCreateDate)) {
            throw new OTPSessionExpiredException("Otp session is expired");
        }
    }
}
