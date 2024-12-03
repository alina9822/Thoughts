package com.alibu.chatapp.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
//Lombok will generate a constructor with all the required fields (final and @NonNull fields) as arguments.
//otherwise we had to write the constructor ourselves or autowire the fields
public class UserService {

    private final UserRepository userRepository;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(null);
    }

    public User saveUser(User user) {
        if (getUserByEmail(user.getEmail()) != null) {
            return null;
        }
        user.setState(Status.ONLINE);
        return userRepository.save(user);
    }

    public void disconnect(User user) {
        var storedUser = getUserByEmail(user.getEmail());
        if (storedUser != null) {
            storedUser.setState(Status.OFFLINE);
            userRepository.save(storedUser);
        }
    }

    public List<UserResponseDto> getAllUsers()
    {
        List<User> users = userRepository.findAll();
        List<UserResponseDto> newUsers = new ArrayList<>();
        for (User user : users) {
            newUsers.add(
                    UserResponseDto
                            .builder()
                            .email(user.getEmail())
                            .firstname(user.getFirstname())
                            .lastname(user.getLastname())
                            .webId(user.getWebId())
                            .state(user.getState())
                            .build());
        }
        return newUsers;
    }


    public List<UserResponseDto> findConnectedUsers() {
        List<User> users = userRepository.findAllByState(Status.ONLINE);
        List<UserResponseDto> newUsers = new ArrayList<>();
        for (User user : users) {
            newUsers.add(
                    UserResponseDto
                            .builder()
                            .email(user.getEmail())
                            .firstname(user.getFirstname())
                            .lastname(user.getLastname())
                            .webId(user.getWebId())
                            .state(user.getState())
                            .build());
        }
        return newUsers;
    }
}
