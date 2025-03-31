package pl.adamik.library.components.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.adamik.library.components.user.dto.UserDto;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

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
}