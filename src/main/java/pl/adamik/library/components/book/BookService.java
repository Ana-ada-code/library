package pl.adamik.library.components.book;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.adamik.library.components.book.dto.BookDetails;
import pl.adamik.library.components.book.dto.BookDto;
import pl.adamik.library.components.book.dto.BookLoanDto;
import pl.adamik.library.components.book.exeption.BookNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
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

    List<BookLoanDto> findBookLoans(Long id) {
        return bookRepository.findById(id)
                .map(Book::getLoans)
                .orElseThrow(BookNotFoundException::new)
                .stream()
                .map(BookLoanMapper::toDto)
                .collect(Collectors.toList());
    }

    public BookDetails findBookDetails(String isbn) {
        String url = UriComponentsBuilder.fromHttpUrl("https://openlibrary.org/api/books")
                .queryParam("bibkeys", "ISBN:{isbn}")
                .queryParam("format", "json")
                .queryParam("jscmd", "data")
                .buildAndExpand(isbn)
                .toUriString();

        String jsonResponse = restTemplate.getForObject(url, String.class);

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode bookNode = rootNode.path("ISBN:" + isbn);

            int numberOfPages = bookNode.path("number_of_pages").asInt(0);
            String coverImage = bookNode.path("cover").path("large").asText("");
            String publishDate = bookNode.path("publish_date").asText("");

            List<String> publishers = new ArrayList<>();
            for (JsonNode publisherNode : bookNode.path("publishers")) {
                publishers.add(publisherNode.path("name").asText());
            }

            return new BookDetails(numberOfPages, coverImage, publishDate, publishers);
        } catch (Exception e) {
            log.error("Error while fetching book details from OpenLibrary for ISBN: {}", isbn, e);
            return new BookDetails(0, "", "", new ArrayList<>());
        }
    }

    @Transactional
    public boolean deleteBook(Long id) {
        bookRepository.deleteById(id);
        return true;
    }

}
