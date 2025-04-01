package pl.adamik.library.components.book;

import org.springframework.stereotype.Service;
import pl.adamik.library.components.book.dto.BookDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    List<BookDto> findByTitleOrAuthorOrIsbn(String text) {
        return bookRepository.findAllByTitleOrAuthorOrIsbn(text)
                .stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }
}
