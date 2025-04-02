package pl.adamik.library.components.book;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.adamik.library.components.book.dto.BookDto;
import pl.adamik.library.components.book.dto.BookLoanDto;
import pl.adamik.library.components.book.exeption.BookNotFoundException;

import java.util.List;
import java.util.Optional;
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

    Optional<BookDto> findById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDto);
    }

    @Transactional
    BookDto save(BookDto book) {
        Book bookEntity = bookMapper.toEntity(book);
        Book savedBook = bookRepository.save(bookEntity);
        return bookMapper.toDto(savedBook);
    }

    List<BookLoanDto> getBookLoans(Long id) {
        return bookRepository.findById(id)
                .map(Book::getLoans)
                .orElseThrow(BookNotFoundException::new)
                .stream()
                .map(BookLoanMapper::toDto)
                .collect(Collectors.toList());
    }
}
