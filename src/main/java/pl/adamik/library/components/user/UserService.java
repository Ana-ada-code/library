package pl.adamik.library.components.user;

import org.springframework.stereotype.Service;
import pl.adamik.library.components.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
}