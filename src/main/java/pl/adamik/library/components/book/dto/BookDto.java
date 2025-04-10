package pl.adamik.library.components.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookDto {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private String genre;
}
