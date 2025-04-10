package pl.adamik.library.components.book.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookDto {
        private Long id;
        @NotBlank(message = "Title is required")
        private String title;
        @NotBlank(message = "Author is required")
        private String author;
        @NotBlank(message = "ISBN is required")
        private String isbn;
        @NotBlank(message = "Genre is required")
        private String genre;
}
