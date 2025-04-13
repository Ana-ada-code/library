package pl.adamik.library.components.book.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookDto {
        final private Long id;
        @NotBlank(message = "Title is required")
        final private String title;
        @NotBlank(message = "Author is required")
        final private String author;
        @NotBlank(message = "ISBN is required")
        final private String isbn;
        @NotBlank(message = "Genre is required")
        final private String genre;
}
