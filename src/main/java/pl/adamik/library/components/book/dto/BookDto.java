package pl.adamik.library.components.book.dto;

public record BookDto(
        Long id,
        String title,
        String author,
        String isbn,
        String genre) {
}
