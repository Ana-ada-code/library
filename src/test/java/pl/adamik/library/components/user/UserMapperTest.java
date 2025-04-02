package pl.adamik.library.components.user;

import org.junit.jupiter.api.Test;
import pl.adamik.library.components.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    @Test
    void shouldMapUserToUserDto() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setPesel("12345678901");

        // When
        UserDto userDto = UserMapper.toDto(user);

        // Then
        assertThat(userDto).isNotNull();
        assertThat(userDto.id()).isEqualTo(user.getId());
        assertThat(userDto.firstName()).isEqualTo(user.getFirstName());
        assertThat(userDto.lastName()).isEqualTo(user.getLastName());
        assertThat(userDto.pesel()).isEqualTo(user.getPesel());
    }

    @Test
    void shouldMapUserDtoToUser() {
        // Given
        UserDto userDto = new UserDto(2L, "Anna", "Nowak", "09876543210");

        // When
        User user = UserMapper.toEntity(userDto);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userDto.id());
        assertThat(user.getFirstName()).isEqualTo(userDto.firstName());
        assertThat(user.getLastName()).isEqualTo(userDto.lastName());
        assertThat(user.getPesel()).isEqualTo(userDto.pesel());
    }

}