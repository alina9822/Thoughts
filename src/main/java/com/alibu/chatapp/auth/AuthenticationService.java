package com.alibu.chatapp.auth;

import com.alibu.chatapp.config.JwtService;
import com.alibu.chatapp.token.Token;
import com.alibu.chatapp.token.TokenRepository;
import com.alibu.chatapp.token.TokenType;
import com.alibu.chatapp.user.Status;
import com.alibu.chatapp.user.User;
import com.alibu.chatapp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    private void revokeAllUser(User user) {
        var validUserToken = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserToken.isEmpty())
            return;
        validUserToken.forEach(t ->
                {
                    t.setExpired(true);
                    t.setRevoked(true);
                }
        );
        tokenRepository.saveAll(validUserToken);
    }



    public String makeUserName(String email)
    {
        String[] str1 = email.split("@");
        String[] str2 = str1[1].split("[.]");
        Random rand = new Random();
        return str1[0]+str2[0]+rand.nextInt(1000);
    }


    public AuthResponseDto register(RegisterRequestDto request) {
        Optional<User> userOptional =
                repository.findByEmail(request.email());

        if (userOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }

        var user_name =makeUserName(request.email());

        var user = User.builder()
                .webId(user_name)
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .state(Status.ONLINE)
                .build();

        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(savedUser);
        saveUserToken(savedUser, jwtToken);

        return AuthResponseDto.builder()
                .token(jwtToken)
                .firstname(savedUser.getFirstname())
                .lastname(savedUser.getLastname())
                .email(savedUser.getEmail())
                .webId(savedUser.getWebId())
                .role(savedUser.getRole())
                .build();
    }

    public AuthResponseDto authenticate(AuthRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        var user = repository.findByEmail(request.email())
                .orElseThrow();
        user.setState(Status.ONLINE);
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        revokeAllUser(user);
        saveUserToken(user, jwtToken);

        var response = AuthResponseDto.builder()
                .token(jwtToken)
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .webId(user.getWebId())
                .role(user.getRole())
                .build();

        System.out.println(response);
        return response;
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}
