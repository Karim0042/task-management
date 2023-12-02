package az.iktlab.taskmanagment

import az.iktlab.taskmanagment.config.CustomEventPublisher
import az.iktlab.taskmanagment.entity.Role
import az.iktlab.taskmanagment.entity.User
import az.iktlab.taskmanagment.event.NewUserRegistrationEvent
import az.iktlab.taskmanagment.model.jwt.JwtToken
import az.iktlab.taskmanagment.model.request.UserSignInRequest
import az.iktlab.taskmanagment.model.request.UserSignUpRequest
import az.iktlab.taskmanagment.repository.OTPSessionRepository
import az.iktlab.taskmanagment.repository.RoleRepository
import az.iktlab.taskmanagment.repository.UserRepository
import az.iktlab.taskmanagment.security.JWTProvider
import az.iktlab.taskmanagment.service.impl.UserServiceImpl
import org.mockito.Mock
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification
import spock.lang.Subject

class UserServiceTest extends Specification{
    @Subject
    UserServiceImpl userService

    @Mock
    UserRepository userRepository

    @Mock
    RoleRepository roleRepository

    @Mock
    PasswordEncoder passwordEncoder

    @Mock
    JWTProvider jwtProvider

    @Mock
    CustomEventPublisher eventPublisher

    @Mock
    OTPSessionRepository sessionRepository

    def "signUp"() {
        given:
        UserSignUpRequest signUpRequest = new UserSignUpRequest(
                email: "test@gmail.com",
                username: "testuser",
                password: "123456",
                rememberMe: false
        )
        Role role = new Role(name: "USER")
        roleRepository.findRoleByName("USER") >> role
        userRepository.existsByEmailAndIsDeletedFalse(signUpRequest.email) >> false

        when:
        JwtToken result = userService.signUp(signUpRequest)

        then:
        1 * userRepository.save(_ as User)
        1 * eventPublisher.publishEvent(_ as NewUserRegistrationEvent)
        1 * jwtProvider.getJWTToken(_, _)
    }

    def "signUp"() {
        given:
        UserSignUpRequest signUpRequest = new UserSignUpRequest(
                email: "test@gmail.com",
                username: "user",
                password: "12345",
                rememberMe: false
        )
        userRepository.existsByEmailAndIsDeletedFalse(signUpRequest.email) >> true

        when:
        userService.signUp(signUpRequest)

        then:
        thrown(ResourceAlreadyExistsException)
    }

    def "signIn"() {
        given:
        UserSignInRequest signInRequest = new UserSignInRequest(
                email: "test@gmail.com",
                password: "123456",
                rememberMe: false
        )
        User user = new User(
                id: "1",
                username: "test",
                email: "test@gmail.com",
                password: "12345",
                createDate: LocalDateTime.now(),
                isDeleted: false,
                roles: [new Role(name: "USER")]
        )
        userRepository.findByEmailAndIsDeletedFalse(signInRequest.email) >> Optional.of(user)
        passwordEncoder.matches(signInRequest.password, user.password) >> true
        jwtProvider.getJWTToken(user.id, signInRequest.rememberMe) >> "mockedJWTToken"

        when:
        JwtToken result = userService.signIn(signInRequest)

        then:
        result == "mockedJWTToken"
    }

    def "signIn"() {
        given:
        UserSignInRequest signInRequest = new UserSignInRequest(
                email: "test@example.com",
                password: "wrongPassword",
                rememberMe: false
        )
        User user = new User(
                id: "1",
                username: "testuser",
                email: "test@example.com",
                password: "encodedPassword",
                createDate: LocalDateTime.now(),
                isDeleted: false,
                roles: [new Role(name: "USER")]
        )
        userRepository.findByEmailAndIsDeletedFalse(signInRequest.email) >> Optional.of(user)
        passwordEncoder.matches(signInRequest.password, user.password) >> false

        when:
        userService.signIn(signInRequest)

        then:
        thrown(AuthenticationException)
    }

}
