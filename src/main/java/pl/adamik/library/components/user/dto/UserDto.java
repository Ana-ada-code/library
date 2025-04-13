package pl.adamik.library.components.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {
        final private Long id;
        @NotBlank(message = "First name is required")
        final private String firstName;
        @NotBlank(message = "Last name is required")
        final private String lastName;
        @NotBlank(message = "PESEL is required")
        @Pattern(regexp = "\\d{11}", message = "PESEL must be 11 digits")
        final private String pesel;
}
