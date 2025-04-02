package pl.adamik.library.components.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.adamik.library.components.book.Book;
import pl.adamik.library.components.loanHistory.LoanHistory;
import pl.adamik.library.components.user.dto.UserDto;
import pl.adamik.library.components.user.dto.UserLoanHistoryDto;
import pl.adamik.library.components.user.exeption.DuplicatePeselException;
import pl.adamik.library.components.user.exeption.UserNotFoundException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


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
        assertThat(result.get(0).firstName()).isEqualTo("Jan");
        assertThat(result.get(1).firstName()).isEqualTo("Anna");

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
        assertThat(result.get(0).lastName()).containsIgnoringCase(lastName);
        assertThat(result.get(1).lastName()).containsIgnoringCase(lastName);

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
        UserDto userDto = new UserDto(null,"Anna", "Kowalska", "12345678901");
        User userEntity = UserMapper.toEntity(userDto);
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setFirstName("Anna");
        savedUser.setLastName("Kowalska");
        savedUser.setPesel("12345678901");

        when(userRepository.findByPesel(userDto.pesel())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        UserDto result = userService.save(userDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.firstName()).isEqualTo("Anna");
        assertThat(result.lastName()).isEqualTo("Kowalska");
        assertThat(result.pesel()).isEqualTo("12345678901");

        verify(userRepository, times(1)).findByPesel(userDto.pesel());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowDuplicatePeselException_whenPeselAlreadyExists() {
        // Given
        UserDto userDto = new UserDto(null,"Jan", "Nowak", "98765432109");
        User existingUser = new User();
        existingUser.setId(2L);
        existingUser.setFirstName("Jan");
        existingUser.setLastName("Nowak");
        existingUser.setPesel("98765432109");

        when(userRepository.findByPesel(userDto.pesel())).thenReturn(Optional.of(existingUser));

        // When & Then
        assertThatThrownBy(() -> userService.save(userDto))
                .isInstanceOf(DuplicatePeselException.class);

        verify(userRepository, times(1)).findByPesel(userDto.pesel());
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
        assertThat(result.get().id()).isEqualTo(userId);
        assertThat(result.get().firstName()).isEqualTo("Anna");
        assertThat(result.get().lastName()).isEqualTo("Kowalska");
        assertThat(result.get().pesel()).isEqualTo("12345678901");

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
        UserDto userDto = new UserDto(1L, "Anna", "Kowalska", "12345678901");

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

        when(userRepository.findByPesel(userDto.pesel())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        UserDto result = userService.update(userDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.firstName()).isEqualTo("Anna");
        assertThat(result.lastName()).isEqualTo("Nowak");
        assertThat(result.pesel()).isEqualTo("12345678901");

        verify(userRepository, times(1)).findByPesel(userDto.pesel());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowException_whenPeselBelongsToAnotherUser() {
        // Given
        UserDto userDto = new UserDto(2L, "Jan", "Nowak", "12345678901");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFirstName("Anna");
        existingUser.setLastName("Kowalska");
        existingUser.setPesel("12345678901");

        when(userRepository.findByPesel(userDto.pesel())).thenReturn(Optional.of(existingUser));

        // When & Then
        assertThatThrownBy(() -> userService.update(userDto))
                .isInstanceOf(DuplicatePeselException.class);

        verify(userRepository, times(1)).findByPesel(userDto.pesel());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldReturnUserLoanHistories_whenUserExists() {
        // Given
        Long userId = 1L;
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Dune");
        book1.setAuthor("Frank Herbert");
        book1.setIsbn("9780441013593");
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("1984");
        book2.setAuthor("George Orwell");
        book2.setIsbn("9780451524935");

        LoanHistory loan1 = new LoanHistory(10L, LocalDate.of(2024, 1, 10), LocalDate.of(2024, 1, 20), null, book1);
        LoanHistory loan2 = new LoanHistory(11L, LocalDate.of(2024, 2, 5), null, null, book2);

        User user = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .pesel("12345678901")
                .loanHistories(List.of(loan1, loan2))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        List<UserLoanHistoryDto> expectedDtos = user.getLoanHistories().stream()
                .map(UserLoanHistoryMapper::toDto)
                .collect(Collectors.toList());

        // When
        List<UserLoanHistoryDto> result = userService.getUserLoanHistories(userId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(expectedDtos);

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldThrowException_whenUserNotFound() {
        // Given
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getUserLoanHistories(userId))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldReturnEmptyList_whenUserHasNoLoanHistories() {
        // Given
        Long userId = 2L;
        User user = User.builder()
                .id(userId)
                .firstName("Jane")
                .lastName("Doe")
                .pesel("98765432109")
                .loanHistories(List.of())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        List<UserLoanHistoryDto> result = userService.getUserLoanHistories(userId);

        // Then
        assertThat(result).isEmpty();

        verify(userRepository, times(1)).findById(userId);
    }

}