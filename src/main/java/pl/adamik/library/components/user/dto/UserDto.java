package pl.adamik.library.components.user.dto;

public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String pesel) {
}
