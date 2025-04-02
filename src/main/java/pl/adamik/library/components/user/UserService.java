package pl.adamik.library.components.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.adamik.library.components.user.dto.UserDto;
import pl.adamik.library.components.user.dto.UserLoanDto;
import pl.adamik.library.components.user.exeption.DuplicatePeselException;
import pl.adamik.library.components.user.exeption.UserNotFoundException;

import java.util.List;
import java.util.Optional;
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

    List<UserDto> findByLastName(String lastName) {
        return userRepository.findAllByLastNameContainingIgnoreCase(lastName)
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    Optional<UserDto> findById(Long id) {
        return userRepository.findById(id).map(UserMapper::toDto);
    }

    @Transactional
    UserDto save(UserDto user) {
        Optional<User> userByPesel = userRepository.findByPesel(user.pesel());
        userByPesel.ifPresent(u -> {
            throw new DuplicatePeselException();
        });
        return mapAndSaveUser(user);
    }

    @Transactional
    UserDto update(UserDto user) {
        Optional<User> userByPesel = userRepository.findByPesel(user.pesel());
        userByPesel.ifPresent(u -> {
            if (!u.getId().equals(user.id())) {
                throw new DuplicatePeselException();
            }
        });
        return mapAndSaveUser(user);
    }

    private UserDto mapAndSaveUser(UserDto user) {
        User userEntity = UserMapper.toEntity(user);
        User savedUser = userRepository.save(userEntity);
        return UserMapper.toDto(savedUser);
    }

    List<UserLoanDto> getUserLoans(Long userId) {
        return userRepository.findById(userId)
                .map(User::getLoans)
                .orElseThrow(UserNotFoundException::new)
                .stream()
                .map(UserLoanMapper::toDto)
                .collect(Collectors.toList());
    }
}