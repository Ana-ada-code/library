package pl.adamik.library.components.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.adamik.library.components.user.dto.UserDto;
import pl.adamik.library.components.user.exeptions.DuplicatePeselException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Test
    void shouldReturnAllUsers_whenUsersExist() {
        // Given
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Jan");
        user1.setLastName("Kowalski");
        user1.setPesel("12345678901");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Anna");
        user2.setLastName("Nowak");
        user2.setPesel("09876543210");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // When
        List<UserDto> result = userService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFirstName()).isEqualTo("Jan");
        assertThat(result.get(1).getFirstName()).isEqualTo("Anna");

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyList_whenNoUsersExist() {
        // Given
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<UserDto> result = userService.findAll();

        // Then
        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnUsers_whenLastNameMatches() {
        // Given
        String lastName = "Nowak";

        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Anna");
        user1.setLastName("Nowak");
        user1.setPesel("12345678901");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Jan");
        user2.setLastName("Nowakowski");
        user2.setPesel("09876543210");

        when(userRepository.findAllByLastNameContainingIgnoreCase(lastName))
                .thenReturn(List.of(user1, user2));

        // When
        List<UserDto> result = userService.findByLastName(lastName);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getLastName()).containsIgnoringCase(lastName);
        assertThat(result.get(1).getLastName()).containsIgnoringCase(lastName);

        verify(userRepository, times(1)).findAllByLastNameContainingIgnoreCase(lastName);
    }

    @Test
    void shouldReturnEmptyList_whenNoUsersMatch() {
        // Given
        String lastName = "Kowalski";
        when(userRepository.findAllByLastNameContainingIgnoreCase(lastName))
                .thenReturn(List.of());

        // When
        List<UserDto> result = userService.findByLastName(lastName);

        // Then
        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findAllByLastNameContainingIgnoreCase(lastName);
    }

    @Test
    void shouldSaveUser_whenPeselIsUnique() {
        // Given
        UserDto userDto = new UserDto();
        userDto.setFirstName("Anna");
        userDto.setLastName("Kowalska");
        userDto.setPesel("12345678901");
        User userEntity = UserMapper.toEntity(userDto);
        User savedUser = new User(1L, "Anna", "Kowalska", "12345678901");

        when(userRepository.findByPesel(userDto.getPesel())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        UserDto result = userService.save(userDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("Anna");
        assertThat(result.getLastName()).isEqualTo("Kowalska");
        assertThat(result.getPesel()).isEqualTo("12345678901");

        verify(userRepository, times(1)).findByPesel(userDto.getPesel());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowDuplicatePeselException_whenPeselAlreadyExists() {
        // Given
        UserDto userDto = new UserDto();
        userDto.setFirstName("Jan");
        userDto.setLastName("Nowak");
        userDto.setPesel("98765432109");
        User existingUser = new User(2L, "Jan", "Nowak", "98765432109");

        when(userRepository.findByPesel(userDto.getPesel())).thenReturn(Optional.of(existingUser));

        // When & Then
        assertThatThrownBy(() -> userService.save(userDto))
                .isInstanceOf(DuplicatePeselException.class);

        verify(userRepository, times(1)).findByPesel(userDto.getPesel());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldReturnUserDto_whenUserExists() {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setFirstName("Anna");
        user.setLastName("Kowalska");
        user.setPesel("12345678901");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        Optional<UserDto> result = userService.findById(userId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(userId);
        assertThat(result.get().getFirstName()).isEqualTo("Anna");
        assertThat(result.get().getLastName()).isEqualTo("Kowalska");
        assertThat(result.get().getPesel()).isEqualTo("12345678901");

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldReturnEmptyOptional_whenUserDoesNotExist() {
        // Given
        Long userId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        Optional<UserDto> result = userService.findById(userId);

        // Then
        assertThat(result).isEmpty();

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldUpdateUser_whenPeselIsUniqueOrSameUser() {
        // Given
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("Anna");
        userDto.setLastName("Kowalska");
        userDto.setPesel("12345678901");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFirstName("Anna");
        existingUser.setLastName("Kowalska");
        existingUser.setPesel("12345678901");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setFirstName("Anna");
        updatedUser.setLastName("Nowak");
        updatedUser.setPesel("12345678901");

        when(userRepository.findByPesel(userDto.getPesel())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        UserDto result = userService.update(userDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Anna");
        assertThat(result.getLastName()).isEqualTo("Nowak");
        assertThat(result.getPesel()).isEqualTo("12345678901");

        verify(userRepository, times(1)).findByPesel(userDto.getPesel());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowException_whenPeselBelongsToAnotherUser() {
        // Given
        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setFirstName("Jan");
        userDto.setLastName("Nowak");
        userDto.setPesel("12345678901");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFirstName("Anna");
        existingUser.setLastName("Kowalska");
        existingUser.setPesel("12345678901");

        when(userRepository.findByPesel(userDto.getPesel())).thenReturn(Optional.of(existingUser));

        // When & Then
        assertThatThrownBy(() -> userService.update(userDto))
                .isInstanceOf(DuplicatePeselException.class);

        verify(userRepository, times(1)).findByPesel(userDto.getPesel());
        verify(userRepository, never()).save(any(User.class));
    }
}